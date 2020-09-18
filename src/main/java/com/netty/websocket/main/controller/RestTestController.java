package com.netty.websocket.main.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beust.jcommander.Strings;
import com.netty.websocket.main.entity.BirdUsers;
import com.netty.websocket.main.services.UserServices;
import com.netty.websocket.tools.codeworker.UuidUtil;
import com.netty.websocket.tools.password.PasswordUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class RestTestController {

    @Autowired
    UserServices userServices;

    @GetMapping("/test")
    public Object test(String username){
        if (username==null && "".equals(username) && username.length()==0) {
            return false;
        }else{
            return true;
        }
    }

    @GetMapping("/encode")
    public Object encode(String password){
        return PasswordUtils.encode(password);
    }

    @GetMapping("/decode")
    public Object decode(String password,String code){
        return PasswordUtils.matches(password,code);
    }

    @GetMapping("/uuid")
    public Object uuid(String password){
        return UuidUtil.generateStr(64);
    }

}
