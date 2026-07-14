package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.McpClientLoad;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.client.factory.McpLoadFactory;
import org.ryc.ai.domain.agent.service.armory.matter.skills.LoadSkillsService;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    private McpLoadFactory mcpLoadFactory;

    @Resource
    private LoadSkillsService defaultLoadSkillsService;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("开始加载聊天模型- chatModel");


        AiAgentConfigTableVO.Module.ChatModel chatModelParam = armoryCommandEntity.getAiAgentConfigTableVO().getModule().getChatModel();
        OpenAiApi openAiApi = dynamicContext.getOpenAiApi();

        // 装载mcp
        List<ToolCallback> toolCallbacks = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(chatModelParam.getToolMcpList())){
            for (AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp : chatModelParam.getToolMcpList()){
                McpClientLoad mcpClientLoad = mcpLoadFactory.getMcpClientLoad(toolMcp);
                toolCallbacks.addAll(List.of(mcpClientLoad.getToolCallbacks(toolMcp)));
            }

        }
        // 装载 skills
        if(CollectionUtils.isNotEmpty(chatModelParam.getSkillList())){
            for(AiAgentConfigTableVO.Module.ChatModel.Skill skill : chatModelParam.getSkillList()){
                toolCallbacks.addAll(List.of(defaultLoadSkillsService.getSkillsToolCallback(skill)));
            }
        }




        ChatModel chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(chatModelParam.getModel())
                        .toolCallbacks(toolCallbacks)
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








}
