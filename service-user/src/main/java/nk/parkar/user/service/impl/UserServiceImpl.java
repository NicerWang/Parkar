package nk.parkar.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import nk.parkar.user.base.exceptionhandler.exception.TokenNotExitException;
import nk.parkar.user.base.exceptionhandler.exception.UserNotExitException;
import nk.parkar.user.entity.User;
import nk.parkar.user.entity.vo.UserInfoVo;
import nk.parkar.user.entity.vo.UserLoginVo;
import nk.parkar.user.entity.vo.UserUpdateVo;
import nk.parkar.user.mapper.UserMapper;
import nk.parkar.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import nk.parkar.user.utils.JWTUtil;
import nk.parkar.user.utils.MD5;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.List;

/**
 * @author HangYu Li
 * @since 2021-10-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public String login(UserLoginVo userLoginVo) throws Exception{
        String phone = userLoginVo.getPhone();
        String password = userLoginVo.getPassword();
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

    @Override
    public UserInfoVo getInformationOfUser(String userId) throws TokenNotExitException, UserNotExitException {
        if(userId == null){
            throw new TokenNotExitException();
        }
        User user = baseMapper.selectById(userId);
        return new UserInfoVo(
                user.getId(),
                user.getUsername(),
                user.getSex(),
                user.getPhone(),
                user.getAddress(),
                user.getBalance(),
                user.getIsDisabled(),
                user.getRegisterTime(),
                user.getLastLoginTime()
        );
    }

    @Override
    public boolean updateInformationOfUser(String userId, UserUpdateVo newUserInfo) {
        if(userId == null){
            return false;
        }
        User user = baseMapper.selectById(userId);
        String username = newUserInfo.getUsername();
        String password = newUserInfo.getPassword();
        String phone = newUserInfo.getPhone();
        String address = newUserInfo.getAddress();

        if(username != null && username.length() != 0){
            user.setUsername(username);
        }
        if(password != null && password.length() != 0){
            user.setPassword(MD5.encrypt(password));
        }
        if(phone != null && phone.length() != 0){
            user.setPhone(phone);
        }
        if(address != null && address.length() != 0){
            user.setAddress(address);
        }

        baseMapper.updateById(user);
        return true;
    }

    @Override
    public boolean isUserExit(String userId){
        User user = baseMapper.selectById(userId);
        return user != null;
    }

    @Override
    public List<User> getAllUsersInformation() {
        List<User> users = baseMapper.selectList(null);
        return users;
    }


}
