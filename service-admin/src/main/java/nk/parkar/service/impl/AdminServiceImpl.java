package nk.parkar.service.impl;


import nk.parkar.mapper.AdminMapper;
import nk.parkar.pojo.Admin;
import nk.parkar.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {


    private AdminMapper adminMapper;

    @Autowired
    public void setAdminMapper(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public boolean login(String name, String pwd) {
        Admin admin = adminMapper.getByName(name);
        return admin != null && admin.getPwd().equals(pwd);
    }

    @Override
    public boolean updatePwd(String name, String pwd) {
        int ret = adminMapper.updatePwd(name,pwd);
        return ret == 1;
    }
}
