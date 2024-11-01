package com.project.wms.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.wms.entity.Menu;
import com.project.wms.mapper.MenuMapper;
import com.project.wms.service.MenuService;
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
