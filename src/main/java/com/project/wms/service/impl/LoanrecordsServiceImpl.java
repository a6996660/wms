package com.project.wms.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.project.wms.entity.BlzcData;
import com.project.wms.entity.Loanrecords;
import com.project.wms.mapper.LoanrecordsMapper;
import com.project.wms.service.ILoanrecordsService;
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
 * @author jobob
 * @since 2024-08-31
 */
@Service
public class LoanrecordsServiceImpl extends ServiceImpl<LoanrecordsMapper, Loanrecords> implements ILoanrecordsService {
    @Resource
    private LoanrecordsMapper loanrecordsMapper;
    @Override
    public IPage pageC(IPage<Loanrecords> page) {
        return loanrecordsMapper.pageC(page);
    }

    @Override
    public IPage pageCC(IPage<Loanrecords> page, Wrapper wrapper) {
        return loanrecordsMapper.pageCC(page,wrapper);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        List<Loanrecords> data = new ArrayList<>();
        data = this.list();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "日志.xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        EasyExcel.write(response.getOutputStream(), Loanrecords.class).sheet("Sheet1").doWrite(data);
    }
}
