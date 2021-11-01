package nk.parkar.user.controller;


import nk.parkar.user.entity.User;
import nk.parkar.user.service.UserService;
import nk.parkar.user.utils.R;
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

