package com.project.wms.service.ManageService;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.wms.entity.manage.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wms
 * @since 2022-07-21
 */
public interface UserService extends IService<User> {

    IPage pageC(IPage<User> page);

    IPage pageCC(IPage<User> page, Wrapper wrapper);
}
