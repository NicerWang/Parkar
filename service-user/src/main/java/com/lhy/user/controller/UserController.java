package com.lhy.user.controller;


import com.lhy.user.entity.User;
import com.lhy.user.service.UserService;
import com.lhy.user.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * @author HangYu Li
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return R.ok().data("token",token);
    }

    @PostMapping("/register")
    public R register(@RequestBody User user) throws Exception{
        System.out.println(user);
        userService.register(user);
        return R.ok();
    }
}

