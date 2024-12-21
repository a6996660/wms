package com.project.wms.service.ChatGPT.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionRequest;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionResult;
import com.project.wms.common.TenDigitIdGenerator;
import com.project.wms.entity.message.SessionContext;
import com.project.wms.mapper.messageMapper.SessionContextMapper;
import com.project.wms.service.ChatGPT.IDouBaoApi;
import com.project.wms.service.ManageService.ILogService;
import com.volcengine.ark.runtime.model.bot.completion.chat.BotChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DouBaoImpl implements IDouBaoApi {
    private final TenDigitIdGenerator idGenerator = new TenDigitIdGenerator();

    @Value("${app.config.DouBaoApiKey}")
    private String apiKey;

    @Value("${app.config.baseUrl}")
    private String baseUrl;

    @Value("${app.config.model}")
    private String model;

    @Value("${app.config.botId}")
    private String botId;
    @Autowired
    private SessionContextMapper sessionContextMapper;
    @Autowired
    private ILogService logService;

    private final ConcurrentHashMap<String, List<ChatMessage>> inMemorySessionContext = new ConcurrentHashMap<>();

    private static final int MAX_CONTEXT_LENGTH = 10; // 最大上下文长度

    public String chatGPT2(Map<String, String> params, String sessionId, Boolean isBot) {
        try {
            if (params.get("message") == null || params.get("message").isEmpty()) return "消息为空";
            String message = params.get("message");

//           / 获取或初始化会话上下文
            List<ChatMessage> messages = this.getSessionContext(sessionId);

            // 添加系统消息和用户消息
            if (messages.isEmpty()) {
                final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build();
                messages.add(systemMessage);
                // 保存一段会话上下文
                this.saveSessionContext(sessionId, systemMessage);
            }
            final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();
            messages.add(userMessage);
            // 保存一段会话上下文
            this.saveSessionContext(sessionId, userMessage);

            // 限制上下文长度
            while (messages.size() > MAX_CONTEXT_LENGTH) {
                messages.remove(0); // 删除最早的几条消息,防止缓存溢出
            }
            // 创建服务实例
            ArkService service = ArkService.builder().apiKey(apiKey).baseUrl(baseUrl).build();
            // 发送请求并获取响应
            AtomicReference<String> returnMessage = new AtomicReference<>("");
            if (isBot){
                System.out.println(messages);
                BotChatCompletionRequest chatCompletionRequest = BotChatCompletionRequest.builder()
                        .botId(botId) //bot-20241125225426-wzcrl 为您当前的智能体的ID，注意此处与Chat API存在差异。差异对比详见 SDK使用指南
                        .messages(messages)
                        .build();
                BotChatCompletionResult chatCompletionResult =  service.createBotChatCompletion(chatCompletionRequest);
                chatCompletionResult.getChoices().forEach(
                        choice -> {
                            String content = choice.getMessage().getContent().toString();
                            returnMessage.set(content);
                            // 将助手的回复添加到上下文中
                            ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(content).build();
                            messages.add(assistantMessage);
                            // 保存一段会话上下文
                            this.saveSessionContext(sessionId, assistantMessage);
                        }
                );
            }else{
                System.out.println(messages);
                // 构建请求
                ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                        .model(model)
                        .messages(messages)
                        .build();
                service.createChatCompletion(chatCompletionRequest).getChoices().forEach(
                        choice -> {
                            String content = choice.getMessage().getContent().toString();
                            returnMessage.set(content);
                            // 将助手的回复添加到上下文中
                            ChatMessage assistantMessage = ChatMessage.builder().role(ChatMessageRole.ASSISTANT).content(content).build();
                            messages.add(assistantMessage);
                            // 保存一段会话上下文
                            this.saveSessionContext(sessionId, assistantMessage);
                        }
                );
            }
            // 打印返回的消息内容
//            System.out.println(returnMessage);
            //更新内存中的会话上下文
            inMemorySessionContext.put(sessionId, messages);
            // 关闭服务
            service.shutdownExecutor();

            // 返回消息内容
            return returnMessage.get();
        } catch (Exception e) {
            logService.insertLog("错误信息", "error", e.toString(), "system", "豆包API调用出错");
            return e.toString();
        }
    }

    /**
     * 获取会话上下文
     *
     * @param sessionId
     * @return
     */

    public List<ChatMessage> getSessionContext(String sessionId) {
        // 先从内存中获取
        List<ChatMessage> messages = inMemorySessionContext.get(sessionId);
        if (messages == null) {
            messages = new ArrayList<>();
            // 如果内存中没有，从数据库中获取
            List<SessionContext> sessionContextList = sessionContextMapper.getBySessionIdOrderByDate(sessionId);
            if (sessionContextList.size() > 0) {
                for (SessionContext sessionContext : sessionContextList) {
                    String messagesJson = sessionContext.getMessages();
                    ChatMessage chatMessage = JSON.parseObject(messagesJson, new TypeReference<ChatMessage>() {
                    });
                    messages.add(chatMessage);
                }
                inMemorySessionContext.put(sessionId, messages);
            }
        }
        return messages;
    }

    /**
     * 保存会话上下文
     *
     * @param sessionId
     * @param
     */
    public void saveSessionContext(String sessionId, ChatMessage chatMessage) {
        // 更新内存中的数据
        // 更新数据库中的数据
        SessionContext sessionContext = new SessionContext();
        sessionContext.setId(idGenerator.nextId());
        sessionContext.setSessionId(sessionId);
        sessionContext.setMessages(JSON.toJSONString(chatMessage));
        sessionContextMapper.insert(sessionContext);
    }
    
}
