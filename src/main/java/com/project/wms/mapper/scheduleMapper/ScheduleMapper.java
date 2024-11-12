package com.project.wms.mapper.scheduleMapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.project.wms.entity.schedule.Taskschedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2024-08-29
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Taskschedule> {
    IPage pageC(IPage<Taskschedule> page);

    IPage pageCC(IPage<Taskschedule> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
