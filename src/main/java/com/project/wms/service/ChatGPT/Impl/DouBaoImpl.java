package com.project.wms.service.ChatGPT.Impl;

import com.project.wms.service.ManageService.ILogService;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.project.wms.service.ChatGPT.IDouBaoApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public String chatGPT(Map<String,String> params) {
        try{
            if (params.get("message") == null || params.get("message").equals("")) return "消息为空";
            String message = params.get("message");
            ArkService service = ArkService.builder().apiKey(apiKey).baseUrl(baseUrl).build();

            /*----- standard request -----*/
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build();
            final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(message).build();
            messages.add(systemMessage);
            messages.add(userMessage);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .build();
            AtomicReference<String> returnMessage = new AtomicReference<>("");
            service.createChatCompletion(chatCompletionRequest).getChoices().forEach(
                    choice -> returnMessage.updateAndGet(v -> v + choice.getMessage().getContent())
            );

            System.out.println(returnMessage);
//        final List<ChatMessage> streamMessages = new ArrayList<>();
//        final ChatMessage streamSystemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content("你是豆包，是由字节跳动开发的 AI 人工智能助手").build();
//        final ChatMessage streamUserMessage = ChatMessage.builder().role(ChatMessageRole.USER).content("常见的十字花科植物有哪些？").build();
//        streamMessages.add(streamSystemMessage);
//        streamMessages.add(streamUserMessage);
//
//        ChatCompletionRequest streamChatCompletionRequest = ChatCompletionRequest.builder()
//                .model("ep-20241117183407-pk7f6")
//                .messages(streamMessages)
//                .build();
//
//        service.streamChatCompletion(streamChatCompletionRequest)
//                .doOnError(Throwable::printStackTrace)
//                .blockingForEach(
//                        choice -> {
//                            if (choice.getChoices().size() > 0) {
//                                System.out.print(choice.getChoices().get(0).getMessage().getContent());
//                            }
//                        }
//                );

            // shutdown service
            service.shutdownExecutor();
            return returnMessage.get();
        }catch (Exception e){
            logService.insertLog("错误信息", "error", e.toString(), "system", "豆包API调用出错");
            return e.toString();
        }
    }
}
