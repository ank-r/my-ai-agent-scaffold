package org.ryc.ai.test.api.tool;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Duration;

/**
 * @ClassName SpringAiToolTest
 * @Description
 * @Author admin
 * @Time 2026/7/12 9:41
 * @Version 1.0
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringAiToolTest {

    @Value("${temp.deepseek.key}")
    String deepSeekKey ;

    @Test
    public void testMcp() {

        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(deepSeekKey)
                .build();

        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("deepseek-v4-flash")
                        .toolCallbacks(SyncMcpToolCallbackProvider.builder().mcpClients(buildMacTools()).build().getToolCallbacks())
                        .build())
                .build();


        String response = chatModel.call("你 有那些工具可以用");
        log.info("返回： " + response);

    }

    public McpSyncClient buildMacTools(){
        HttpClientSseClientTransport httpClientSseClientTransport = HttpClientSseClientTransport
                .builder("http://localhost:8101")
                .sseEndpoint("/sse")
                .build();

        McpSyncClient mcpSyncClient = McpClient.sync(httpClientSseClientTransport)

                .requestTimeout(Duration.ofSeconds(60))
                .build();

        try {
            mcpSyncClient.initialize();
            log.info("MCP 客户端初始化成功");
        } catch (Exception e) {
            log.error("MCP 客户端初始化失败（请检查 API Key 是否有效、网络是否可达）: {}", e.getMessage());
            throw e;
        }
        return mcpSyncClient;
    }
}
