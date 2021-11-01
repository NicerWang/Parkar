package nk.parkar.service.impl;


import nk.parkar.mapper.AdminMapper;
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
        return adminMapper.getByName(name).getPwd().equals(pwd);
    }
}
