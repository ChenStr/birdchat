package com.netty.websocket.main.controller;

import com.netty.websocket.main.entity.BirdUsers;
import com.netty.websocket.main.services.UserServices;
import com.netty.websocket.tools.password.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServices userServices;

    /**
     * 通过用户的id去查找用户
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/getUser")
    public String getUserById(String id, Model model){
        BirdUsers users = userServices.getById(id);
        model.addAttribute("user",users);
        return "/pages/userList";
    }

}
