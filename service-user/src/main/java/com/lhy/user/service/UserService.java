package com.lhy.user.service;

import com.lhy.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author HangYu Li
 * @since 2021-10-30
 */
public interface UserService extends IService<User> {
    public String login(User user) throws Exception;

    public boolean register(User user) throws Exception;
}
