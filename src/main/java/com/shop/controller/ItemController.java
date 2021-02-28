package com.shop.controller;

import com.shop.controller.viewobject.ItemView;
import com.shop.error.BusinessException;
import com.shop.response.CommonReturnType;
import com.shop.service.CacheService;
import com.shop.service.ItemService;
import com.shop.service.model.ItemModel;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller("item")
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheService cacheService;

    //创建商品
    @RequestMapping(value = "/create", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FROMED})
    @ResponseBody
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description") String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock") Integer stock,
                                       @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {
        //封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setStock(stock);
        itemModel.setImgUrl(imgUrl);
        ItemModel itemModelForReturn = itemService.createItem(itemModel);
        ItemView itemView = convertFromModelToView(itemModelForReturn);
        return CommonReturnType.create(itemView);
    }

    @GetMapping("/get/{id}")
    public String detailPage(@PathVariable("id") Integer id, Map<String, Object> map){
        map.put("goodID", id);
        return "mall/item";
    }


    //商品详情页浏览
    @PostMapping("/get")
    @ResponseBody
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) {
        ItemModel itemModel;
        //第一级，先取本地缓存
        itemModel = (ItemModel) cacheService.getFromCommonCache("item_" + id);

        if (itemModel == null) {
            //第二级，根据商品id到redis中获取
            itemModel = (ItemModel) redisTemplate.opsForValue().get("item_" + id);
            if (itemModel == null) {
                itemModel = itemService.getItemById(id);
                //将itemModel放入redis
                redisTemplate.opsForValue().set("item_" + id, itemModel);
                redisTemplate.expire("item_" + id, 10, TimeUnit.MINUTES);
            }
            //填充cache
            cacheService.setCommonCache("item_" + id, itemModel);
        }

        ItemView itemView = convertFromModelToView(itemModel);
        return CommonReturnType.create(itemView);
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listItem() {
        List<ItemModel> itemModelList = itemService.listItem();
        List<ItemView> itemViewList = itemModelList.stream().map(itemModel -> {
            ItemView itemView = this.convertFromModelToView(itemModel);
            return itemView;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemViewList);
    }

    private ItemView convertFromModelToView(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemView itemView = new ItemView();
        BeanUtils.copyProperties(itemModel, itemView);
        if (itemModel.getPromoModel() != null) {
            //有正在进行/即将进行的秒杀活动
            itemView.setPromoStatus(itemModel.getPromoModel().getStatus());
            //itemView.setPromoStatus(1);
            itemView.setPromoId(itemModel.getPromoModel().getId());
            itemView.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemView.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        } else {
            itemView.setPromoStatus(0);
        }
        return itemView;
    }

}
