package com.project.wms.controller.manageController;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.wms.common.QueryPageParam;
import com.project.wms.common.Result;
import com.project.wms.entity.manage.Log;
import com.project.wms.service.ManageService.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author dingxhui
 * @since 2024-11-16
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private ILogService logService;

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        logService.export(response);
    }

    @GetMapping("/list")
    public List<Log> list() {
        return logService.list();
    }

    //根据电话号码插叙n
    @GetMapping("/findById")
    public Result findByNo(@RequestParam String no) {
        List list = logService.lambdaQuery().eq(Log::getId, no).list();
        return list.size() > 0 ? Result.suc(list) : Result.fail();
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Log bzdata) {
        return logService.save(bzdata) ? Result.suc() : Result.fail();
    }

    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Log bzdata) {
        return logService.updateById(bzdata) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id) {
        return logService.removeById(id) ? Result.suc() : Result.fail();
    }


    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody Log blzcData) {
        return logService.updateById(blzcData);
    }

    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody Log blzcData) {
        return logService.saveOrUpdate(blzcData);
    }

    //删除
    @GetMapping("/delete")
    public boolean delete(Integer id) {
        return logService.removeById(id);
    }

    //查询（模糊、匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody Log blzcData) {
        LambdaQueryWrapper<Log> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(blzcData.getName())) {
            lambdaQueryWrapper.like(Log::getName, blzcData.getName());
        }

        return Result.suc(logService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
//    public List<Log> listPage(@RequestBody HashMap map){
    public List<Log> listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        System.out.println("name===" + (String) param.get("name"));
        /*System.out.println("no==="+(String)param.get("no"));*/
        /*LambdaQueryWrapper<Log> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Log::getUserName,blzcData.getUserName());

        return logService.list(lambdaQueryWrapper);*/

        Page<Log> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Log> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(Log::getName, name);


        IPage result = logService.page(page, lambdaQueryWrapper);

        System.out.println("total==" + result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC")
    public List<Log> listPageC(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        System.out.println("name===" + (String) param.get("name"));


        Page<Log> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Log> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(Log::getName, name);


        //IPage result = logService.pageC(page);
        IPage result = logService.pageCC(page, lambdaQueryWrapper);

        System.out.println("total==" + result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query) {
        LambdaQueryWrapper<Log> lambdaQueryWrapper = new LambdaQueryWrapper();
        HashMap param = query.getParam();
        String name = param.get("name") == null ? "" : (String) param.get("name");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (param.get("startDate") != null && !"".equals(param.get("startDate"))) {
            LocalDateTime startDate = LocalDateTime.parse(param.get("startDate").toString(), formatter);
            //时间在startDate之后
            lambdaQueryWrapper.ge(Log::getCreatetime, startDate);
        }
        if (param.get("endDate") != null && !"".equals(param.get("endDate"))) {
            LocalDateTime endDate = LocalDateTime.parse(param.get("endDate").toString(), formatter);
            lambdaQueryWrapper.le(Log::getCreatetime, endDate);
        }
        Page<Log> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Log::getName, name);
        }
        lambdaQueryWrapper.orderByDesc(Log::getCreatetime);
        //IPage result = logService.pageC(page);
        IPage result = logService.pageCC(page, lambdaQueryWrapper);

        System.out.println("total==" + result.getTotal());

        return Result.suc(result.getRecords(), result.getTotal());
    }

}
