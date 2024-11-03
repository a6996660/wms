package com.project.wms.service.HFWeather.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.project.wms.service.HFWeather.heFengWeatherService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

@Service
public class heFengWeatherServiceImpl implements heFengWeatherService {

    // 请求天气数据的城市代码字典
    private static final Map<String, String> locationIDMap = new HashMap<>();

    static {
        locationIDMap.put("北京", "101010100");
        locationIDMap.put("莱州", "101120502");
        locationIDMap.put("威海", "101121301");
    }

    // 配置
//    private static final Map<String, Object> pushConfig = new HashMap<>();
//
//    static {
//        pushConfig.put("wxMessage_url", "http://192.168.5.139:3001/webhook/msg/v2?token=dingxhui"); // 微信机器人地址
//        pushConfig.put("weather_api", "cd294325a22e4aa78eff7c8d1f665e7c"); // 和风天气api key
//        pushConfig.put("try_times", 3); // 重试次数
//    }

    public String getWeather(String city, String apiKey) {
        int tryTimes = 1;
        String cityCode = locationIDMap.get(city);
        String url = "https://devapi.qweather.com/v7/weather/3d?location=" + cityCode + "&key=" + apiKey;
        try {
            System.out.println("请求URL：" + url);

            // 创建 HttpClient 对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 创建 HttpGet 对象
            HttpGet httpGet = new HttpGet(url);

            // 添加请求头
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-Type", "application/json");
            httpGet.setHeader("User-Agent", "Java-HttpClient");

            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    // 读取响应内容
                    String content = EntityUtils.toString(entity, "UTF-8");

                    JSONObject weatherData = JSON.parseObject(content);
                    if (weatherData.containsKey("code") && !weatherData.getString("code").equals("200")){
                        throw new RuntimeException("请求失败，错误信息：" + weatherData.toString());
                    }
                    JSONArray daily = weatherData.getJSONArray("daily");
                    return daily.toJSONString();
                }
            } else {
                System.out.println("请求失败，状态码：" + responseCode);
            }
        } catch (Exception e) {
            System.out.println("请求过程中发生错误：" + e.getMessage());
        }
        return null;
    }

    public String formatWeather(String cityName, String weatherResult) {
        JSONArray daily = JSON.parseArray(weatherResult);

        StringBuilder returnText = new StringBuilder();
        for (int i = 0; i < daily.size(); i++) {
            JSONObject day = daily.getJSONObject(i);
            String date = day.getString("fxDate");
            String textDay = day.getString("textDay");
            String textNight = day.getString("textNight");
            String tempMax = day.getString("tempMax");
            String tempMin = day.getString("tempMin");

            String textDay2Night = textDay.equals(textNight) ? textDay : textDay + "转" + textNight;
            String description = cityName + "：" + textDay2Night + "，" + tempMin + "-" + tempMax + "度 \n";
            returnText.append(description);
            break; // 仅展示第一天
        }
        return returnText.toString();
    }

    public void sendWebhookMessage(String message, boolean isRoom, String name, String url) {
        Map<String, Object> body = new HashMap<>();
        body.put("to", name);
        body.put("isRoom", isRoom);
        Map<String, String> data = new HashMap<>();
        data.put("content", message);
        body.put("data", data);

        String jsonBody = JSON.toJSONString(body);

        try {
            // 创建 HttpClient 对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 创建 HttpPost 对象
            HttpPost httpPost = new HttpPost(url);

            // 设置请求头
            httpPost.setHeader("Content-Type", "application/json");

            // 设置请求体
            StringEntity entity = new StringEntity(jsonBody, "UTF-8");
            httpPost.setEntity(entity);

            // 执行请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                System.out.println("消息发送成功");
            } else {
                System.out.println("消息发送失败，状态码: " + responseCode);
                HttpEntity errorEntity = response.getEntity();
                if (errorEntity != null) {
                    String content = EntityUtils.toString(errorEntity, "UTF-8");
                    System.out.println("响应内容: " + content);
                }
            }
        } catch (Exception e) {
            System.out.println("发送消息过程中发生错误：" + e.getMessage());
        }
    }


    public String weatherService(Map<String,Object> param) {
        if (param == null || param.get("city") == null || param.get("api") == null || param.get("url") == null)
            return "参数错误";
        List<String> paramKeys = Arrays.asList("city", "api", "url", "isRoom", "name");
        for (String key : paramKeys) {
            if (!param.containsKey(key))
                return "参数" + key + "为空";
        }
        List<String> cityList = (List<String>) param.get("city"); // 城市列表
        String apiKey = param.get("api").toString(); 
        String url = param.get("url").toString();
        Boolean isRoom = (Boolean) param.get("isRoom"); // 是否为群聊
        String name = param.get("name").toString(); // 发送人
        StringBuilder weatherMessage = new StringBuilder("今日天气情况：\n");
        for (String city : cityList) {
            weatherMessage.append(formatWeather(city, getWeather(city, apiKey)));
        }
        sendWebhookMessage(weatherMessage.toString(), isRoom, name,url);
        return "success";
    }
}
