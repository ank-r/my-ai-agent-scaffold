package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.model.valobj.enums.AgentTypeEnum;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.ryc.ai.domain.agent.service.armory.node.workflow.LoopAgentNode;
import org.ryc.ai.domain.agent.service.armory.node.workflow.ParallelAgentNode;
import org.ryc.ai.domain.agent.service.armory.node.workflow.SequentialAgentNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName AgentWorkflowNode
 * @Description
 * @Author admin
 * @Time 2026/7/12 15:10
 * @Version 1.0
 */

@Service
@Slf4j
public class AgentWorkflowNode extends AbstractArmorySupport {

    @Resource
    private LoopAgentNode loopAgentNode;

    @Resource
    private ParallelAgentNode parallelAgentNode;

    @Resource
    private SequentialAgentNode sequentialAgentNode;

    @Resource
    private RunnerNode runnerNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        log.info("Ai Agent 装配操作 - AgentWorkflowNode");


        List<AiAgentConfigTableVO.Module.AgentWorkflow> agentWorkflows = armoryCommandEntity.getAiAgentConfigTableVO().getModule().getAgentWorkflows();

        if(CollectionUtils.isEmpty(agentWorkflows) || dynamicContext.getCurrentAgentWorkflowIndex() >= agentWorkflows.size() ){
            log.info("Ai Agent 装配操作 - AgentWorkflowNode - agentWorkflows is empty");
            dynamicContext.setCurrentAgentWorkflow(null);
            return router(armoryCommandEntity, dynamicContext);
        }

        dynamicContext.setCurrentAgentWorkflow(agentWorkflows.get(dynamicContext.getCurrentAgentWorkflowIndex()));
        dynamicContext.addAgentWorkflowIndex();

        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {


        if(Objects.isNull(dynamicContext.getCurrentAgentWorkflow())){
            return runnerNode ;
        }

        AiAgentConfigTableVO.Module.AgentWorkflow agentWorkflow = dynamicContext.getCurrentAgentWorkflow();

        String type = agentWorkflow.getType();
        AgentTypeEnum agentTypeEnum = AgentTypeEnum.formType(type);

        if (null == agentTypeEnum){
            throw new RuntimeException("agentWorkflow type is error!");
        }

        String node = agentTypeEnum.getNode();

        return switch (node){
            case "loopAgentNode" -> loopAgentNode;
            case "parallelAgentNode" -> parallelAgentNode;
            case "sequentialAgentNode" -> sequentialAgentNode;
            default -> runnerNode;
        };
    }

}
