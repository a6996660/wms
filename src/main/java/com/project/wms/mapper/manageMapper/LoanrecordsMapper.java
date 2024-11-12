package com.project.wms.mapper.manageMapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.project.wms.entity.manage.Loanrecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2024-08-31
 */
@Mapper
public interface LoanrecordsMapper extends BaseMapper<Loanrecords> {
    IPage pageC(IPage<Loanrecords> page);

    IPage pageCC(IPage<Loanrecords> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
