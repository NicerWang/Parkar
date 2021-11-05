package nk.parkar.user.service;

import nk.parkar.user.base.exceptionhandler.exception.TokenNotExitException;
import nk.parkar.user.base.exceptionhandler.exception.UserNotExitException;
import nk.parkar.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import nk.parkar.user.entity.vo.UserInfoVo;
import nk.parkar.user.entity.vo.UserLoginVo;
import nk.parkar.user.entity.vo.UserUpdateVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HangYu Li
 * @since 2021-10-30
 */
public interface UserService extends IService<User> {
    public String login(UserLoginVo userLoginVo) throws Exception;

    public void register(User user) throws Exception;

    public UserInfoVo getInformationOfUser(String userId) throws TokenNotExitException, UserNotExitException;

    public boolean updateInformationOfUser(String userId, UserUpdateVo newUserInfo);

    public boolean isUserExit(String userId);

}
