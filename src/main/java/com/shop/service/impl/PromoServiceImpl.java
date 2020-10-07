package com.shop.service.impl;

import com.shop.dao.PromoDataObjMapper;
import com.shop.dao.dataobject.PromoDataObj;
import com.shop.service.PromoService;
import com.shop.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoDataObjMapper promoDataObjMapper;

    @Override
    public PromoModel getPromoByItemId(Integer itemId) {
        PromoDataObj promoDataObj = promoDataObjMapper.selectByItemId(itemId);
        //service层需要model，而不是dataobj
        PromoModel promoModel = convertFromDataObjToPromoModel(promoDataObj);
        if(promoModel == null){
            return null;
        }
        //判断当前时间是否秒杀活动即将开始或正在进行
        //DateTime noww = new DateTime();
        if(promoModel.getStartDate().isAfterNow()){
            promoModel.setStatus(1);
        }else if(promoModel.getEndDate().isBeforeNow()){
            promoModel.setStatus(3);
        }else{
            promoModel.setStatus(2);
        }

        return promoModel;
    }

    private PromoModel convertFromDataObjToPromoModel(PromoDataObj promoDataObj){
        if(promoDataObj == null){
            return null;
        }
        PromoModel promoModel = new PromoModel();
        BeanUtils.copyProperties(promoDataObj, promoModel);
        promoModel.setStartDate(new DateTime(promoDataObj.getStartDate()));
        promoModel.setEndDate(new DateTime(promoDataObj.getEndDate()));
        return promoModel;
    }

}
