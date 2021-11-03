package nk.parkar.controller;

import nk.parkar.service.AdminService;
import nk.parkar.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/update")
public class UpdateController {
    private AdminService adminService;

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/pwd")
    public String updatePwd(@RequestParam("pwd") String pwd, HttpServletRequest request){
        String token = request.getHeader("token");
        if(!JWTUtil.checkAdmin(token)){
            return "ERROR";
        }
        if(adminService.updatePwd(JWTUtil.check(token),pwd))
            return "SUCCESS";
        else return "ERROR";
    }
}
