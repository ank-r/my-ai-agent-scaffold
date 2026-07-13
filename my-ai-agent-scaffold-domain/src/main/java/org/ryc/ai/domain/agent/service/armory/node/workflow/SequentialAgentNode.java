package org.ryc.ai.domain.agent.service.armory.node.workflow;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.ryc.ai.domain.agent.service.armory.node.AbstractArmorySupport;
import org.ryc.ai.domain.agent.service.armory.node.RunnerNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName SequentialAgentNode
 * @Description
 * @Author admin
 * @Time 2026/7/12 15:09
 * @Version 1.0
 */

@Service("sequentialAgentNode")
@Slf4j
public class SequentialAgentNode extends AbstractArmorySupport {

    @Autowired
    private RunnerNode runnerNode;


    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 装配操作 - sequentialAgentNode ");

        List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = dynamicContext.getAgentWorkflows();
        AiAgentConfigTableVO.Module.AgentWorkflow agentWorkflow = agentWorkflows.remove(0);

        List<BaseAgent> subAgent = dynamicContext.getAgentFromAgentGroup(agentWorkflow.getSubAgents());
        SequentialAgent sequentialAgent = SequentialAgent.builder()
                .name(agentWorkflow.getName())
                .description(agentWorkflow.getDescription())
                .subAgents(subAgent)
                .build();

        dynamicContext.getAgentGroup().put(agentWorkflow.getName(), sequentialAgent);
        dynamicContext.setSequentialAgent(sequentialAgent);

        registerBean(agentWorkflow.getName(), SequentialAgent.class, sequentialAgent);

        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        return runnerNode;
    }
}
