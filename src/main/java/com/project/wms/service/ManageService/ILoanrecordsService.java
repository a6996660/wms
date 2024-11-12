package com.project.wms.service.ManageService;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.wms.entity.manage.Loanrecords;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jobob
 * @since 2024-08-31
 */
public interface ILoanrecordsService extends IService<Loanrecords> {
    IPage pageC(IPage<Loanrecords> page);

    IPage pageCC(IPage<Loanrecords> page, Wrapper wrapper);
    
    public void export(HttpServletResponse response) throws IOException;
}
