package org.ryc.ai.test.api.agent;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ryc.ai.domain.agent.model.entity.ChatCommandEntity;
import org.ryc.ai.domain.agent.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatServiceTest {

    @Resource
    private ChatService chatService;

    @Value("classpath:file/dog.png")
    private org.springframework.core.io.Resource imageResource;

    @Test
    public void test_handleMessage_01() {
        List<String> message = chatService.handleMessage("100002", "dashuihua", "你具备哪些能力");
        log.info("测试结果:{}", JSON.toJSONString(message));
    }

    @Test
    public void test_handleMessage_04_withImage() throws IOException {
        String agentId = "100002";
        String userId = "dashuihua";

        String sessionId = chatService.createSession(agentId, userId);

        ChatCommandEntity chatCommandEntity = ChatCommandEntity.builder()
                .agentId(agentId)
                .userId(userId)
                .sessionId(sessionId)
                .texts(List.of(new ChatCommandEntity.Content.Text("请识别这个图片。告诉我它是什么动物，并用一句话描述。")))
                .files(List.of())
                .inlineDatas(List.of(new ChatCommandEntity.Content.InlineData(imageResource.getContentAsByteArray(), MimeTypeUtils.IMAGE_PNG_VALUE)))
                .build();

        List<String> message = chatService.handleMessage(chatCommandEntity);
        log.info("测试结果:{}", JSON.toJSONString(message));
    }

}
