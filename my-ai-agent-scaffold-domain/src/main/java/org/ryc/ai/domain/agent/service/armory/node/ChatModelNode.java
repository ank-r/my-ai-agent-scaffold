package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.HttpClientSseClientTransport;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.jackson.JacksonMcpJsonMapper;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName ChatModelNode
 * @Description
 * @Author admin
 * @Time 2026/7/12 11:08
 * @Version 1.0
 */
@Service
@Slf4j
public class ChatModelNode extends  AbstractArmorySupport{

    @Resource
    private AgentNode agentNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("开始加载聊天模型- chatModel");


        AiAgentConfigTableVO.Module.ChatModel chatModelParam = armoryCommandEntity.getAiAgentConfigTableVO().getModule().getChatModel();
        OpenAiApi openAiApi = dynamicContext.getOpenAiApi();


        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(chatModelParam.getModel())
                        .toolCallbacks(SyncMcpToolCallbackProvider.builder()
                                .mcpClients(getAllMcpCliAllent(chatModelParam.getToolMcpList()))
                                .build().getToolCallbacks())
                        .build())
                .build();

        dynamicContext.setChatModel(chatModel);

        AiAgentRegisterVO aiAgentRegisterVO = new AiAgentRegisterVO();
        aiAgentRegisterVO.setDynamicContext(dynamicContext);

        return router(armoryCommandEntity, dynamicContext);
    }



    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return agentNode;
    }



    private List<McpSyncClient> getAllMcpCliAllent(List<AiAgentConfigTableVO.Module.ChatModel.ToolMcp> toolMcpList) {

        return Optional.ofNullable(toolMcpList).orElse(Lists.newArrayList()).stream().map(this::buildMcpClient).collect(Collectors.toList());

    }

    private McpSyncClient buildMcpClient(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {

        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.SSEServerParameters sseServerParameters = toolMcp.getSse();
        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.StdioServerParameters stdioServerParameters = toolMcp.getStdio();

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

            return mcpSyncClient;

        }

        if(stdioServerParameters  != null){
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

            return mcpSyncClient ;

        }



        throw new RuntimeException("tool mcp sse and stdio is null!");

    }


}
