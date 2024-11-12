package com.project.wms.controller.manageController;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.wms.common.QueryPageParam;
import com.project.wms.common.Result;
import com.project.wms.entity.manage.BlzcData;
import com.project.wms.service.ManageService.IBlzcDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2024-08-29
 */
@RestController
@RequestMapping("/blzc-data")
public class BlzcDataController {

    @Autowired
    private IBlzcDataService iBlzcDataService;
    

    @GetMapping("/list")
    public List<BlzcData> list(){
        return iBlzcDataService.list();
    }
    //根据电话号码插叙n
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no){
        List list = iBlzcDataService.lambdaQuery().eq(BlzcData::getTelNum,no).list();
        return list.size()>0?Result.suc(list):Result.fail();
    }
    //新增
    @PostMapping("/save")
    public Result save(@RequestBody BlzcData bzdata){
        return iBlzcDataService.save(bzdata)?Result.suc():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody BlzcData bzdata){
        return iBlzcDataService.updateById(bzdata)?Result.suc():Result.fail();
    }
    //删除
    @GetMapping("/del")
    public Result del(@RequestParam String id){
        return iBlzcDataService.removeById(id)?Result.suc():Result.fail();
    }
    

    //修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody BlzcData blzcData){
        return iBlzcDataService.updateById(blzcData);
    }
    //新增或修改
    @PostMapping("/saveOrMod")
    public boolean saveOrMod(@RequestBody BlzcData blzcData){
        return iBlzcDataService.saveOrUpdate(blzcData);
    }
    //删除
    @GetMapping("/delete")
    public boolean delete(Integer id){
        return iBlzcDataService.removeById(id);
    }

    //查询（模糊、匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody BlzcData blzcData){
        LambdaQueryWrapper<BlzcData> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(blzcData.getUserName())){
            lambdaQueryWrapper.like(BlzcData::getUserName,blzcData.getUserName());
        }

        return Result.suc(iBlzcDataService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
//    public List<BlzcData> listPage(@RequestBody HashMap map){
    public List<BlzcData> listPage(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        System.out.println("name==="+(String)param.get("name"));
        /*System.out.println("no==="+(String)param.get("no"));*/
        /*LambdaQueryWrapper<BlzcData> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(BlzcData::getUserName,blzcData.getUserName());

        return iBlzcDataService.list(lambdaQueryWrapper);*/

        Page<BlzcData> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<BlzcData> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(BlzcData::getUserName,name);


        IPage result = iBlzcDataService.page(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC")
    public List<BlzcData> listPageC(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String name = (String)param.get("name");
        System.out.println("name==="+(String)param.get("name"));



        Page<BlzcData> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<BlzcData> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(BlzcData::getUserName,name);


        //IPage result = iBlzcDataService.pageC(page);
        IPage result = iBlzcDataService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return result.getRecords();
    }

    @PostMapping("/listPageC1")
    public Result listPageC1(@RequestBody QueryPageParam query){
        HashMap param = query.getParam();
        String businessId = (String)param.get("businessId");
        Page<BlzcData> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<BlzcData> lambdaQueryWrapper = new LambdaQueryWrapper();
        if(StringUtils.isNotBlank(businessId) && !"null".equals(businessId)){
            lambdaQueryWrapper.like(BlzcData::getBusinessId,businessId);
        }
//        if(StringUtils.isNotBlank(sex)){
//            lambdaQueryWrapper.eq(BlzcData::getSex,sex);
//        }
//        if(StringUtils.isNotBlank(roleId)){
//            lambdaQueryWrapper.eq(BlzcData::getRoleId,roleId);
//        }
        
        //IPage result = iBlzcDataService.pageC(page);
        IPage result = iBlzcDataService.pageCC(page,lambdaQueryWrapper);

        System.out.println("total=="+result.getTotal());

        return Result.suc(result.getRecords(),result.getTotal());
    }


    @PostMapping("/changeMoney")
    public Result changeMoney(@RequestBody Map<String,Object> body){
        String message = iBlzcDataService.changeMoney(body);
        if (message != null){
            return Result.fail();
        }
        return Result.suc(message);
    }

    @PostMapping("/importExcel")
    public Result importExcel(@RequestParam("file") MultipartFile file) {
        iBlzcDataService.importExcel(file);
        return  Result.suc();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        iBlzcDataService.export(response);
    }
}
