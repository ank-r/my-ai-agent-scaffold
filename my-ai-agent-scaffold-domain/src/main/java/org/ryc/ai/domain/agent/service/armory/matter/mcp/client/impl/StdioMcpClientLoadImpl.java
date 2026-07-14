package org.ryc.ai.domain.agent.service.armory.matter.mcp.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.McpClientLoad;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @ClassName StdioMcpClientLoadImpl
 * @Description
 * @Author admin
 * @Time 2026/7/13 22:15
 * @Version 1.0
 */
@Service
@Slf4j
public class StdioMcpClientLoadImpl implements McpClientLoad {
    @Override
    public ToolCallback[] getToolCallbacks(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {


        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters stdioServerParameters = toolMcp.getStdio();


        if(stdioServerParameters != null){

            AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters.ServerParameters serverParameters = stdioServerParameters.getServerParameters();


            ServerParameters stdioParams = ServerParameters.builder(serverParameters.getCommand())
                    .args(serverParameters.getArgs())
                    .env(serverParameters.getEnv())
                    .build();

            StdioClientTransport stdioClientTransport = new StdioClientTransport(stdioParams, new JacksonMcpJsonMapper(new ObjectMapper()));

            McpSyncClient mcpSyncClient = McpClient
                    .sync(stdioClientTransport)
                    .requestTimeout(Duration.ofSeconds(stdioServerParameters.getRequestTimeout()))
                    .build();

            McpSchema.InitializeResult initialize = mcpSyncClient.initialize();

            log.info("tool stdio mcp initialize {}", initialize);

            return SyncMcpToolCallbackProvider.builder().mcpClients(mcpSyncClient).build().getToolCallbacks();
        }


        return new ToolCallback[0];
    }
}
