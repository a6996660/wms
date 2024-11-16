package com.project.wms.mapper.manageMapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.project.wms.entity.manage.Loanrecords;
import com.project.wms.entity.manage.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author dingxhui
 * @since 2024-11-16
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

    IPage pageC(IPage<Log> page);

    IPage pageCC(IPage<Log> page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
