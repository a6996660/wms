package com.project.wms.service.MessageService.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.JSONArray;
import com.project.wms.entity.message.WeChatMessage;
import com.project.wms.service.ChatGPT.IDouBaoApi;
import com.project.wms.service.MessageService.IMessageService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.wms.service.ManageService.ILogService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    private ILogService logService;
    @Autowired
    private IDouBaoApi douBaoApi;
    @Value("${wechat.config.url}")
    private String wxMessage_url;

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
                    if (weatherData.containsKey("code") && !weatherData.getString("code").equals("200")) {
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

    public String sendWebhookMessage(String message, boolean isRoom, String name, String url) {
        Map<String, Object> body = new HashMap<>();
        body.put("to", name);
        body.put("isRoom", isRoom);
        Map<String, String> data = new HashMap<>();
        data.put("content", message);
        body.put("data", data);
        if (url == null || url.isEmpty()) {
            url = wxMessage_url;
        }

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
                return "消息发送成功";
            } else {
                System.out.println("消息发送失败，状态码: " + responseCode);
                HttpEntity errorEntity = response.getEntity();
                if (errorEntity != null) {
                    String content = EntityUtils.toString(errorEntity, "UTF-8");
                    System.out.println("响应内容: " + content);
                    return "消息发送失败:" + content;
                }
                return "消息发送失败，状态码: " + responseCode;
            }
        } catch (Exception e) {
            System.out.println("发送消息过程中发生错误：" + e.getMessage());
            return "发送消息过程中发生错误：" + e.getMessage();
        }
    }


    public String weatherService(Map<String, Object> param) {
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
        return sendWebhookMessage(weatherMessage.toString(), isRoom, name, url);
    }

//    {
//        // 消息来自群，会有以下对象，否则为空字符串
//        "room": {
//        "id": "@@xxx",
//                "topic": "abc" // 群名
//        "payload": {
//            "id": "@@xxxx",
//                    "adminIdList": [],
//            "avatar": "xxxx", // 相对路径，应该要配合解密
//                    "memberList": [
//            {
//                id: '@xxxx',
//                        avatar: "http://localhost:3001/resouces?media=%2Fcgi-bin%2Fmmwebwx-bixxx", //请配合 token=[YOUR_PERSONAL_TOKEN] 解密
//                    name:'昵称',
//                    alias: '备注名'/** 个人备注名，非群备注名 */ }
//        ]
//        },
//        //以下暂不清楚什么用途，如有兴趣，请查阅 wechaty 官网文档
//        "_events": {},
//        "_eventsCount": 0,
//    },
//
//
//        // 消息来自个人，会有以下对象，否则为空字符串
//        "to": {
//        "id": "@xxx",
//
//                "payload": {
//            "alias": "", //备注名
//                    "avatar": "http://localhost:3001/resouces?media=%2Fcgi-bin%2Fmmwebwx-bixxx", //请配合 token=[YOUR_PERSONAL_TOKEN] 解密
//                    "friend": false,
//                    "gender": 1,
//                    "id": "@xxx",
//                    "name": "xxx",
//                    "phone": [],
//            "signature": "hard mode",
//                    "star": false,
//                    "type": 1
//        },
//
//        "_events": {},
//        "_eventsCount": 0,
//    },
//
//        // 消息发送方
//        "from": {
//        "id": "@xxx",
//
//                "payload": {
//            "alias": "",
//                    "avatar": "http://localhost:3001/resouces?media=%2Fcgi-bin%2Fmmwebwx-bixxx", //请配合 token=[YOUR_PERSONAL_TOKEN] 解密
//                    "city": "北京",
//                    "friend": true,
//                    "gender": 1,
//                    "id": "@xxxx",
//                    "name": "abc", //昵称
//                    "phone": [],
//            "province": "北京",
//                    "star": false,
//                    "type": 1
//        },
//
//        "_events": {},
//        "_eventsCount": 0,
//    }
//
//    }

    /**
     * 接消息api
     *
     * @param body
     * @return
     */

    @Override
    public String receiveMessage(Map<String, Object> body) {
        Map<String, Object> data = new HashMap<>();
        if (body == null || body.get("type") == null) {
            logService.insertLog("接收消息", "receiveMessage", "空参数", "system", "参数错误");
            return "参数错误";
        }
        String logMessage = "";
        //消息体
        String message = "";
        String roomName = ""; //群聊名
        String name = ""; //发送人
        Boolean isRoom = false;
        Boolean isEnable = false; //是否有消息
        try {
            //判断是否被@的群消息
            if (body.get("isMentioned") != null && "1".equals(body.get("isMentioned").toString())) {
                if (body.get("content") != null && body.get("type") != null && "text".equals(body.get("type"))) {
                    message = body.get("content").toString();//@丁某某2号 你好
                    if (message.contains("@丁某某2号")) {
                        message = message.replace("@丁某某2号", "");
                        isEnable = true;
                    }
                    //获取source数据
                    if (body.get("source") != null) {
                        JSONObject source = JSONObject.parseObject(body.get("source").toString());
                        //拿到群聊数据
                        if (source.get("room") != null) {
                            JSONObject room = JSONObject.parseObject(source.get("room").toString());
                            if (room.get("payload") != null) {
                                JSONObject payload = JSONObject.parseObject(room.get("payload").toString());
                                if (payload.get("topic") != null) {
                                    roomName = payload.get("topic").toString();
                                    isRoom = true;
                                }
                            }
                        }
                        //谁@的我
                        if (source.get("from") != null) {
                            JSONObject from = JSONObject.parseObject(source.get("from").toString());
                            if (from.get("payload") != null) {
                                JSONObject payload = JSONObject.parseObject(from.get("payload").toString());
                                if (payload.get("name") != null && payload.get("id") != null) {
                                    //拿到发送人
                                    name = payload.get("name").toString();
                                    String id = payload.get("id").toString();
                                }
                            }
                        }
                    }
                }
            } else if (body.get("source") != null && body.get("type") != null && "text".equals(body.get("type"))) {//个人数据
                if (body.get("content") != null) {
                    message = body.get("content").toString();
                    isEnable = true;
                    //获取source数据
                    if (body.get("source") != null) {
                        JSONObject source = JSONObject.parseObject(body.get("source").toString());
                        if (source.get("to") != null && source.get("room") != null) {
                            JSONObject room = JSONObject.parseObject(source.get("room").toString());
                            if (room.get("payload") != null) {
                                return "不需要处理消息";
                            }
//                        JSONObject to = JSONObject.parseObject(source.get("to").toString());
//                        if (to.get("payload") != null) {
//                            JSONObject payload = JSONObject.parseObject(to.get("payload").toString());
//                            if (payload.get("name") != null && payload.get("id") != null) {
//                                //拿到发送人
//                                name = payload.get("name").toString();
//                                isRoom = false;
//                                String id = payload.get("id").toString();
//                            }
//                        }
                            if (source.get("from") != null) {
                                JSONObject from = JSONObject.parseObject(source.get("from").toString());
                                if (from.get("payload") != null) {
                                    JSONObject payload = JSONObject.parseObject(from.get("payload").toString());
                                    if (payload.get("name") != null && payload.get("id") != null) {
                                        //拿到发送人
                                        name = payload.get("name").toString();
                                        isRoom = false;
                                        String id = payload.get("id").toString();
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }catch (Exception e){
            logService.insertLog("接收消息", "error", e.toString(), "system", "豆包接收消息异常");
            return "接收消息异常";
        }
        if (isEnable) {
            Map<String, String> params = new HashMap<>();
            if (isRoom) {
                logMessage = "收到群聊【" + roomName + "】来自【" + name + "】的消息：" + message;
                message = name + ":" + message;
                name = roomName;
                logService.insertLog("接收消息", "receiveMessage", logMessage, "system", "群聊消息");
            } else {
                logMessage = "收到来自【" + name + "】的消息：" + message;
                logService.insertLog("接收消息", "receiveMessage", logMessage, "system", "个人消息");
            } 
            params.put("message", message);     
            String result = douBaoApi.chatGPT2(params,name);
            return sendWebhookMessage(result, isRoom, name, wxMessage_url);
        }
        return "没有发送消息";
    }
    
    public WeChatMessage receiveWeChatMessage(Map<String, Object> body){
        WeChatMessage weChatMessage = new WeChatMessage();
        if (body == null || body.get("type") == null) {
            logService.insertLog("接收消息", "receiveMessage", "空参数", "system", "参数错误");
            weChatMessage.setLog("参数错误");
            return weChatMessage;
        }
        String logMessage = "";
        //消息体
        String message = "";
        String roomName = ""; //群聊名
        String name = ""; //发送人
        Boolean isRoom = false;
        Boolean isEnable = false; //是否有消息
        try {
            //判断是否被@的群消息
            if (body.get("isMentioned") != null && "1".equals(body.get("isMentioned").toString())) {
                if (body.get("content") != null && body.get("type") != null && "text".equals(body.get("type"))) {
                    message = body.get("content").toString();//@丁某某2号 你好
                    if (message.contains("@丁某某2号")) {
                        message = message.replace("@丁某某2号", "");
                        isEnable = true;
                    }
                    //获取source数据
                    if (body.get("source") != null) {
                        JSONObject source = JSONObject.parseObject(body.get("source").toString());
                        //拿到群聊数据
                        if (source.get("room") != null) {
                            JSONObject room = JSONObject.parseObject(source.get("room").toString());
                            if (room.get("payload") != null) {
                                JSONObject payload = JSONObject.parseObject(room.get("payload").toString());
                                if (payload.get("topic") != null) {
                                    roomName = payload.get("topic").toString();
                                    isRoom = true;
                                }
                            }
                        }
                        //谁@的我
                        if (source.get("from") != null) {
                            JSONObject from = JSONObject.parseObject(source.get("from").toString());
                            if (from.get("payload") != null) {
                                JSONObject payload = JSONObject.parseObject(from.get("payload").toString());
                                if (payload.get("name") != null && payload.get("id") != null) {
                                    //拿到发送人
                                    name = payload.get("name").toString();
                                    String id = payload.get("id").toString();
                                }
                            }
                        }
                    }
                }
            } else if (body.get("source") != null && body.get("type") != null && "text".equals(body.get("type"))) {//个人数据
                if (body.get("content") != null) {
                    message = body.get("content").toString();
                    isEnable = true;
                    //获取source数据
                    if (body.get("source") != null) {
                        JSONObject source = JSONObject.parseObject(body.get("source").toString());
                        if (source.get("to") != null && source.get("room") != null) {
                            JSONObject room = JSONObject.parseObject(source.get("room").toString());
                            if (room.get("payload") != null) {
                                weChatMessage.setLog("不需要处理消息");
                                return weChatMessage;
                            }
//                        JSONObject to = JSONObject.parseObject(source.get("to").toString());
//                        if (to.get("payload") != null) {
//                            JSONObject payload = JSONObject.parseObject(to.get("payload").toString());
//                            if (payload.get("name") != null && payload.get("id") != null) {
//                                //拿到发送人
//                                name = payload.get("name").toString();
//                                isRoom = false;
//                                String id = payload.get("id").toString();
//                            }
//                        }
                            if (source.get("from") != null) {
                                JSONObject from = JSONObject.parseObject(source.get("from").toString());
                                if (from.get("payload") != null) {
                                    JSONObject payload = JSONObject.parseObject(from.get("payload").toString());
                                    if (payload.get("name") != null && payload.get("id") != null) {
                                        //拿到发送人
                                        name = payload.get("name").toString();
                                        isRoom = false;
                                        String id = payload.get("id").toString();
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }catch (Exception e){
            logService.insertLog("接收消息", "error", e.toString(), "system", "豆包接收消息异常");
            weChatMessage.setLog("不需要处理消息");
            return weChatMessage;
        }
        return weChatMessage;
    }
}
