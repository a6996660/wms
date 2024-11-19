package com.project.wms.service.ChatGPT;

import java.util.Map;

public interface IDouBaoApi {
//    String chatGPT(Map<String,String> params);
    String chatGPT2(Map<String, String> params, String sessionId);
}
