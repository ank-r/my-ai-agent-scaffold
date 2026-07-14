package org.ryc.ai.domain.agent.service.armory.matter.mcp.client.impl;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.McpClientLoad;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @ClassName SeeMcpClientLoadImpl
 * @Description
 * @Author admin
 * @Time 2026/7/13 22:09
 * @Version 1.0
 */
@Service
@Slf4j
public class SeeMcpClientLoadImpl  implements McpClientLoad {
    @Override
    public ToolCallback[] getToolCallbacks(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {


        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.SSEServerParameters sseServerParameters = toolMcp.getSse();

        if(sseServerParameters != null){

            HttpClientSseClientTransport httpClientSseClientTransport = HttpClientSseClientTransport.builder(sseServerParameters.getBaseUri())
                    .sseEndpoint(sseServerParameters.getSseEndpoint())
                    .build();

            McpSyncClient mcpSyncClient = McpClient
                    .sync(httpClientSseClientTransport)
                    .requestTimeout(Duration.ofSeconds(30))
                    .build();



            McpSchema.InitializeResult initialize = mcpSyncClient.initialize();

            log.info("tool see mcp initialize {}", initialize);

            return SyncMcpToolCallbackProvider.builder().addMcpClient(mcpSyncClient).build().getToolCallbacks();

        }


        return new ToolCallback[0];


    }




}
