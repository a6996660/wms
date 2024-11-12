package com.project.wms.mapper.manageMapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.project.wms.entity.manage.BlzcData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface BlzcDataMapper extends BaseMapper<BlzcData> {
    IPage pageC(IPage<BlzcData> page);

    IPage pageCC(IPage<BlzcData> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
