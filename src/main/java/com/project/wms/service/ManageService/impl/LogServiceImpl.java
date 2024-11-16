package com.project.wms.service.ManageService.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.wms.common.TenDigitIdGenerator;
import com.project.wms.entity.manage.Loanrecords;
import com.project.wms.entity.manage.Log;
import com.project.wms.mapper.manageMapper.LoanrecordsMapper;
import com.project.wms.mapper.manageMapper.LogMapper;
import com.project.wms.service.ManageService.ILogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dingxhui
 * @since 2024-11-16
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {
    @Resource
    private LogMapper logMapper;
    @Override
    public IPage pageC(IPage<Log> page) {
        return logMapper.pageC(page);
    }

    @Override
    public IPage pageCC(IPage<Log> page, Wrapper wrapper) {
        return logMapper.pageCC(page,wrapper);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        List<Log> data = new ArrayList<>();
        data = this.list();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "日志.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        EasyExcel.write(response.getOutputStream(), Loanrecords.class).sheet("Sheet1").doWrite(data);
    }

    @Override
    public void insertLog(String name, String type, String message, String operator, String remark) {
        Log log = new Log();
        Long id = new TenDigitIdGenerator().nextId();
        log.setId(id);
        log.setName(name);
        log.setType(type);
        log.setMessage(message);
        log.setOperator(operator);
        log.setRemark(remark);
        this.save(log);
    }

}
