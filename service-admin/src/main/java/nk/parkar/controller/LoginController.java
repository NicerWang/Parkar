package nk.parkar.controller;

import nk.parkar.pojo.Admin;
import nk.parkar.service.AdminService;
import nk.parkar.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public String login(@RequestBody Admin admin) throws Exception {
        if(adminService.login(admin.getName(),admin.getPwd()))
            return JWTUtil.Sign(admin.getName(),true);
        else return "Error";
    }
}

