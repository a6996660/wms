package com.project.wms.controller.HFController;

import com.project.wms.service.HFWeather.heFengWeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/weather")
public class heFengWeatherController {
    @Autowired
    private heFengWeatherService heFengWeatherService;
    
    @RequestMapping("/sendWeatherMessage")
    public String sendWeatherMessage(@RequestBody Map<String, Object> body) {
        return heFengWeatherService.weatherService(body);
            
    }
}
