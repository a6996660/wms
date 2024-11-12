package com.project.wms.service.ManageService.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.wms.entity.manage.Menu;
import com.project.wms.mapper.manageMapper.MenuMapper;
import com.project.wms.service.ManageService.MenuService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wms
 * @since 2022-10-04
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}
