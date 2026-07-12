package org.ryc.ai.test.api.model;

import dev.langchain4j.model.openai.OpenAiChatModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName LangChain4jApiTest
 * @Description
 * @Author admin
 * @Time 2026/7/11 17:52
 * @Version 1.0
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LangChain4jApiTest {

    @Value("${temp.deepseek.key}")
    String deepSeekKey ;

    @Test
    public void langChain4jApiTest() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(deepSeekKey)
                .modelName("deepseek-v4-flash")
                .build();


        String chat = model.chat("hi 你好哇!");
        log.info("测试结果:{}", chat);
    }
}
