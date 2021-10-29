package com.lhy.user.service.impl;

import com.lhy.user.entity.User;
import com.lhy.user.mapper.UserMapper;
import com.lhy.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author HangYu Li
 * @since 2021-10-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
