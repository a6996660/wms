package com.project.wms.controller;

import com.project.wms.service.ChatGPT.IDouBaoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {
    @Autowired
    private IDouBaoApi douBaoApi;
    @RequestMapping("/testDoubao")
    public void testDoubao( @RequestBody Map<String, String> body){
        Map<String,String> params = body;
        String sessionId = body.get("sessionId");
        System.out.println(douBaoApi.chatGPT2(body,sessionId));
    }
    
}
