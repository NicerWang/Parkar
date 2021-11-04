package nk.parkar.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import nk.parkar.user.entity.User;
import nk.parkar.user.mapper.UserMapper;
import nk.parkar.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import nk.parkar.user.utils.JWTUtil;
import nk.parkar.user.utils.MD5;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;

/**
 * @author HangYu Li
 * @since 2021-10-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(User user) throws Exception{
        String phone = user.getPhone();
        String password = user.getPassword();
        if(!StringUtils.hasLength(phone) || !StringUtils.hasLength(password)) {
            throw new Exception("login failed!");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        User phoneUser = baseMapper.selectOne(wrapper);
        if(phoneUser == null){
            throw new Exception("login failed!");
        }
        if(!MD5.encrypt(password).equals(phoneUser.getPassword())) {
            throw new Exception("login failed!");
        }
        if(phoneUser.getIsDisabled() == 1) {
            throw new Exception("login failed! This user is disabled");
        }
        phoneUser.setLastLoginTime(new Date(System.currentTimeMillis()));
        baseMapper.updateById(phoneUser);
        return JWTUtil.Sign(phoneUser.getId());
    }

    @Override
    public void register(User user) throws Exception {
        User newUser = new User();
        String username = user.getUsername();
        String password = user.getPassword();
        String phone = user.getPhone();
        String address = user.getAddress();
        String sex = user.getSex();
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(phone) || StringUtils.isEmpty(address) || StringUtils.isEmpty(sex)) {
            throw new Exception("Register Failed");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        Integer count = baseMapper.selectCount(wrapper);
        if(count > 0) {
            throw new Exception("Register Failed");
        }
        newUser.setUsername(username);
        newUser.setPassword(MD5.encrypt(password));
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setSex(sex);
        newUser.setRegisterTime(new Date(System.currentTimeMillis()));
        newUser.setBalance(0);
        newUser.setIsDisabled(0);
        baseMapper.insert(newUser);
    }
}
