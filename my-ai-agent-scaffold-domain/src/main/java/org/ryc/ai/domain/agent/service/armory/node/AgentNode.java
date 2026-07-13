package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.LlmAgent;
import com.google.adk.models.springai.SpringAI;
import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AgentNode
 * @Description
 * @Author admin
 * @Time 2026/7/12 14:39
 * @Version 1.0
 */
@Slf4j
@Service
public class AgentNode extends AbstractArmorySupport {


    @Resource
    private AgentWorkflowNode agentWorkflowNode;
    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 装配操作 - AgentNode - {}");

        ChatModel chatModel = dynamicContext.getChatModel();

        List<AiAgentConfigTableVO.Module.Agent> agents = armoryCommandEntity.getAiAgentConfigTableVO().getModule().getAgents();

        for(AiAgentConfigTableVO.Module.Agent agent : agents){
            log.info("Ai Agent 装配操作 - AgentNode - {}", agent.getName());

            LlmAgent llmAgent = LlmAgent.builder()
                    .name(agent.getName())
                    .model(new SpringAI(chatModel))
                    .description(agent.getDescription())
                    .instruction(agent.getInstruction())
                    .outputKey(agent.getOutputKey())
                    .build();

            dynamicContext.getAgentGroup().put(agent.getName(),llmAgent);

        }


        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return agentWorkflowNode;
    }
}
