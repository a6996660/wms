package com.project.wms.service.HFWeather;

import java.util.Map;

public interface heFengWeatherService {
    String getWeather(String city, String apiKey);
    String formatWeather(String cityName, String weatherResult);
    String sendWebhookMessage(String message, boolean isRoom, String name,String url);
    String weatherService(Map<String,Object> param);
    String receiveMessage(Map<String,Object> body);
}
