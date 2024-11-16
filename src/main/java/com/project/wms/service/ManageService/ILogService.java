package com.project.wms.service.ManageService;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.wms.entity.manage.Loanrecords;
import com.project.wms.entity.manage.Log;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dingxhui
 * @since 2024-11-16
 */
public interface ILogService extends IService<Log> {
    IPage pageC(IPage<Log> page);

    IPage pageCC(IPage<Log> page, Wrapper wrapper);

    public void export(HttpServletResponse response) throws IOException;
    
    public void insertLog(String name, String type, String message, String operator, String remark);

}
