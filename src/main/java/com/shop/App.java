package com.shop;

import com.shop.dao.UserDataObjMapper;
import com.shop.dao.dataobject.UserDataObj;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.shop"})
@RestController
@MapperScan("com.shop.dao")
public class App
{
    @Autowired
    private UserDataObjMapper userDataObjMapper;

    @RequestMapping("/")
    public String home(){
        UserDataObj userDataObj = userDataObjMapper.selectByPrimaryKey(1);
        if(userDataObj == null){
            return "用户对象不存在";
        }else{
            return userDataObj.getName();
        }
    }

    public static void main( String[] args ) {
        SpringApplication.run(App.class, args);
    }
}
