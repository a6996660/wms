package com.project.wms.service.impl;

import com.project.wms.entity.Test;
import com.project.wms.mapper.TestMapper;
import com.project.wms.service.ITestService;
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
