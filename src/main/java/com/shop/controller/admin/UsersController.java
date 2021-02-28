package com.shop.controller.admin;

import com.shop.dao.dataobject.OrderDataObj;
import com.shop.dao.dataobject.UserDataObj;
import com.shop.response.CommonReturnType;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String getAdminOrders(){
        return "admin/users";
    }

    @GetMapping("/users/list")
    @ResponseBody
    public CommonReturnType getAllOrders(){
        List<UserDataObj> userDataObjList = userService.getAllUsers();
        return CommonReturnType.create(userDataObjList);
    }
}
