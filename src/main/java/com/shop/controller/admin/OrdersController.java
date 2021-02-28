package com.shop.controller.admin;

import com.shop.dao.dataobject.OrderDataObj;
import com.shop.response.CommonReturnType;
import com.shop.service.OrderService;
import com.shop.service.model.OrderModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders/list")
    @ResponseBody
    public CommonReturnType getAllOrders(){
        List<OrderDataObj> orderModelList = orderService.getAllOrders();
        return CommonReturnType.create(orderModelList);
    }

    @GetMapping("/orders")
    public String getAdminOrders(){
        return "admin/orders";
    }


    @GetMapping("/order/delete/{orderId}")
    public String detailPage(@PathVariable("orderId") String orderId){
        orderService.deleteOrder(orderId);
        return "admin/orders";
    }
}
