package com.project.wms.controller;


import com.project.wms.entity.Test;
import com.project.wms.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2024-08-24
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private  ITestService testService;
    @GetMapping("/list")
    public List<Test> test(){
        return testService.list();
    }

}
