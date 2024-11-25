package com.project.wms.controller.MessageController;

import com.project.wms.service.MessageService.IMessageService;
import com.project.wms.service.ManageService.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/weather")
public class messageController {
    @Autowired
    private IMessageService IMessageService;
    @Autowired
    private ILogService logService;
    @RequestMapping("/sendWeatherMessage")
    public String sendWeatherMessage(@RequestBody Map<String, Object> body) {
        return IMessageService.weatherService(body);
            
    }

    /**
     * 收消息
     * Methods: POST
     * ContentType: multipart/form-data
     * 
     */
    @RequestMapping("/receiveMessage")
    public String receiveMessage(@RequestParam Map<String, Object> body) {
        return IMessageService.receiveMessage(body);
    }
}
