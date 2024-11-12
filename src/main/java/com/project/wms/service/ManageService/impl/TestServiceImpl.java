package com.project.wms.service.ManageService.impl;

import com.project.wms.entity.manage.Test;
import com.project.wms.mapper.manageMapper.TestMapper;
import com.project.wms.service.ManageService.ITestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2024-08-24
 */
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test> implements ITestService {

}
