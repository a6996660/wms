package com.project.wms.service.ManageService.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.wms.entity.manage.User;
import com.project.wms.mapper.manageMapper.UserMapper;
import com.project.wms.service.ManageService.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2022-07-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Override
    public IPage pageC(IPage<User> page) {
        return userMapper.pageC(page);
    }

    @Override
    public IPage pageCC(IPage<User> page, Wrapper wrapper) {
        return userMapper.pageCC(page,wrapper);
    }
}
