package nk.parkar.user.controller;


import nk.parkar.user.base.exceptionhandler.exception.TokenNotExitException;
import nk.parkar.user.base.exceptionhandler.exception.UserNotExitException;
import nk.parkar.user.entity.User;
import nk.parkar.user.entity.vo.UserInfoVo;
import nk.parkar.user.entity.vo.UserLoginVo;
import nk.parkar.user.entity.vo.UserUpdateVo;
import nk.parkar.user.service.UserService;
import nk.parkar.user.utils.JWTUtil;
import nk.parkar.user.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HangYu Li
 * @since 2021-10-30
 */
@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody UserLoginVo userLoginVo) throws Exception {
        String token = userService.login(userLoginVo);
        return R.ok().data("token",token);
    }

    @PostMapping("/register")
    public R register(@RequestBody User user) throws Exception{
        System.out.println(user);
        userService.register(user);
        return R.ok();
    }

    @GetMapping("/getUserInfo")
    public R getInformationOfUser(HttpServletRequest request) throws TokenNotExitException, UserNotExitException {
        String userId = JWTUtil.check(request.getHeader("token"));
        UserInfoVo user = userService.getInformationOfUser(userId);
        return R.ok().data("userInformation",user);
    }

    @PostMapping("/updateUserInfo")
    public R updateUserInformation(@RequestBody UserUpdateVo newUserInfo, HttpServletRequest request){
        String userId = JWTUtil.check(request.getHeader("token"));
        boolean isSuccess = userService.updateInformationOfUser(userId, newUserInfo);
        if(isSuccess) {
            return R.ok().message("Update successful");
        }
        return R.error().message("Update Failed");
    }

    @GetMapping("/userExit/{userId}")
    public R isUserExit(@PathVariable String userId){
        boolean userExit = userService.isUserExit(userId);
        if(userExit) {
            return R.ok().message("User exit");
        }
        return R.error().message("User not exit");
    }
}

