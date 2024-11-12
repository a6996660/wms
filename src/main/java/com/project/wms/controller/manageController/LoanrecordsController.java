package com.project.wms.controller.manageController;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.wms.common.QueryPageParam;
import com.project.wms.common.Result;
import com.project.wms.entity.manage.Loanrecords;
import com.project.wms.service.ManageService.ILoanrecordsService;
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
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2024-08-31
 */
@RestController
@RequestMapping("/record")
public class LoanrecordsController {

    @Autowired
    private ILoanrecordsService loanrecordsService;


    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        loanrecordsService.export(response);
    }

    @GetMapping("/list")
    public List<Loanrecords> list(){
        return loanrecordsService.list();
    }
    //根据电话号码插叙n
    @GetMapping("/findById")
    public Result findByNo(@RequestParam String no){
        List list = loanrecordsService.lambdaQuery().eq(Loanrecords::getId,no).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Loanrecords bzdata){
        return loanrecordsService.save(bzdata)?Result.suc():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Loanrecords bzdata){
        return loanrecordsService.updateById(bzdata)?Result.suc():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return loanrecordsService.removeById(id)?Result.suc():Result.fail();
    }


    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody Loanrecords blzcData){
        return loanrecordsService.updateById(blzcData);
    }
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody Loanrecords blzcData){
        return loanrecordsService.saveOrUpdate(blzcData);
    }
    //删除
    @GetMapping("/delete")
    public boolean delete(Integer id){
        return loanrecordsService.removeById(id);
    }

    //查询（模糊、匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody Loanrecords blzcData){
        LambdaQueryWrapper<Loanrecords> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(blzcData.getBorrowerName())){
            lambdaQueryWrapper.like(Loanrecords::getBorrowerName,blzcData.getBorrowerName());
        }

        return Result.suc(loanrecordsService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
//    public List<Loanrecords> listPage(@RequestBody HashMap map){
    public List<Loanrecords> listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        System.out.println("name==="+(String)param.get("name"));
        /*System.out.println("no==="+(String)param.get("no"));*/
        /*LambdaQueryWrapper<Loanrecords> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(Loanrecords::getUserName,blzcData.getUserName());

        return loanrecordsService.list(lambdaQueryWrapper);*/

        Page<Loanrecords> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Loanrecords> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(Loanrecords::getBorrowerName,name);


        IPage result = loanrecordsService.page(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC")
    public List<Loanrecords> listPageC(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        System.out.println("name==="+(String)param.get("name"));



        Page<Loanrecords> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Loanrecords> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(Loanrecords::getBorrowerName,name);


        //IPage result = loanrecordsService.pageC(page);
        IPage result = loanrecordsService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query){
        LambdaQueryWrapper<Loanrecords> lambdaQueryWrapper = new LambdaQueryWrapper();
        HashMap param = query.getParam();
        String businessNumber = param.get("businessNumber") == null? "" : (String)param.get("businessNumber");
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (param.get("startDate") != null && !"".equals(param.get("startDate"))){
            LocalDateTime startDate = LocalDateTime.parse(param.get("startDate").toString(), formatter);
            //时间在startDate之后
            lambdaQueryWrapper.ge(Loanrecords::getReceiveTime,startDate);
        }
        if (param.get("endDate") != null && !"".equals(param.get("endDate"))){
            LocalDateTime endDate = LocalDateTime.parse(param.get("endDate").toString(), formatter);
            lambdaQueryWrapper.le(Loanrecords::getReceiveTime,endDate);
        }
        Page<Loanrecords> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        if(StringUtils.isNotBlank(businessNumber) && !"null".equals(businessNumber)){
            lambdaQueryWrapper.like(Loanrecords::getBusinessNumber,businessNumber);
        }
//        if(StringUtils.isNotBlank(sex)){
//            lambdaQueryWrapper.eq(Loanrecords::getSex,sex);
//        }
//        if(StringUtils.isNotBlank(roleId)){
//            lambdaQueryWrapper.eq(Loanrecords::getRoleId,roleId);
//        }
        lambdaQueryWrapper.orderByDesc(Loanrecords::getOperationTime);

        //IPage result = loanrecordsService.pageC(page);
        IPage result = loanrecordsService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return Result.suc(result.getRecords(),result.getTotal());
    }
}
