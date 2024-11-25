package com.project.wms.service.MessageService;

import java.util.Map;

public interface IMessageService {
    String getWeather(String city, String apiKey);
    String formatWeather(String cityName, String weatherResult);

    /**
     * 微信发消息
     * @param message
     * @param isRoom
     * @param name
     * @param url
     * @return
     */
    String sendWebhookMessage(String message, boolean isRoom, String name,String url);

    /**
     * 和风天气接口
     * @param param
     * @return
     */
    String weatherService(Map<String,Object> param);

    /**
     * 微信收消息
     * @param body
     * @return
     */
    String receiveMessage(Map<String,Object> body);
}
