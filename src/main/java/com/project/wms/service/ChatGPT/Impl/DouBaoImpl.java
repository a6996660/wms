package com.project.wms.service.ChatGPT.Impl;

import com.project.wms.service.ChatGPT.IDouBaoApi;
import com.project.wms.service.ManageService.ILogService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DouBaoImpl implements IDouBaoApi {

    @Value("${app.config.DouBaoApiKey}")
    private String apiKey;

    @Value("${app.config.baseUrl}")
    private String baseUrl;

    @Value("${app.config.model}")
    private String model;

    @Autowired
    private ILogService logService;

    private Map<String, List<ChatMessage>> sessionContext = new ConcurrentHashMap<>();
    private static final int MAX_CONTEXT_LENGTH = 50; // 最大上下文长度
    
    public String chatGPT2(Map<String, String> params, String sessionId) {
        try {
            if (params.get("message") == null || params.get("message").isEmpty()) return "消息为空";
            String message = params.get("message");

            // 获取或初始化会话上下文
            List<ChatMessage> messages = sessionContext.computeIfAbsent(sessionId, k -> new ArrayList<>());

            // 添加系统消息和用户消息
            if (messages.isEmpty()) {
                final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build();
                messages.add(systemMessage);
            }
            final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();
            messages.add(userMessage);

            // 限制上下文长度
            while (messages.size() > MAX_CONTEXT_LENGTH) {
                messages.remove(0); // 删除最早的几条消息,防止缓存溢出
            }


            // 构建请求
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .build();

            // 创建服务实例
            ArkService service = ArkService.builder().apiKey(apiKey).baseUrl(baseUrl).build();

            // 发送请求并获取响应
            AtomicReference<String> returnMessage = new AtomicReference<>("");

            service.createChatCompletion(chatCompletionRequest).getChoices().forEach(
                    choice -> {
                        String content = choice.getMessage().getContent().toString();
                        returnMessage.set(content);

                        // 将助手的回复添加到上下文中
                        ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(content).build();
                        messages.add(assistantMessage);
                    }
            );

            // 打印返回的消息内容
//            System.out.println(returnMessage);

            // 关闭服务
            service.shutdownExecutor();

            // 返回消息内容
            return returnMessage.get();
        } catch (Exception e) {
            logService.insertLog("错误信息", "error", e.toString(), "system", "豆包API调用出错");
            return e.toString();
        }
    }
}
