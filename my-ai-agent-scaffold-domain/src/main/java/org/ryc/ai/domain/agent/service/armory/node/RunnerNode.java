package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import com.google.adk.agents.BaseAgent;
import com.google.adk.plugins.BasePlugin;
import com.google.adk.runner.InMemoryRunner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName RunnerNode
 * @Description
 * @Author admin
 * @Time 2026/7/12 16:11
 * @Version 1.0
 */
@Service
@Slf4j
public class RunnerNode extends AbstractArmorySupport{

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        AiAgentConfigTableVO.Agent agent = armoryCommandEntity.getAiAgentConfigTableVO().getAgent();
        InMemoryRunner runner = getRunner(armoryCommandEntity, dynamicContext);



        AiAgentRegisterVO aiAgentRegisterVO = AiAgentRegisterVO.builder()
                .appName(armoryCommandEntity.getAiAgentConfigTableVO().getAppName())
                .agentId(agent.getAgentId())
                .agentName(agent.getAgentName())
                .agentDesc(agent.getAgentDesc())
                .runner(runner)
                .build();

        registerBean(armoryCommandEntity.getAiAgentConfigTableVO().getAgent().getAgentId(), AiAgentRegisterVO.class, aiAgentRegisterVO);

        return aiAgentRegisterVO;
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return defaultStrategyHandler;
    }

    private InMemoryRunner getRunner(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        AiAgentConfigTableVO.Agent agent = armoryCommandEntity.getAiAgentConfigTableVO().getAgent();


        String runnerAgentName = armoryCommandEntity.getAiAgentConfigTableVO().getModule().getRunner().getAgentName();

        if(StringUtils.isBlank(runnerAgentName)){
            throw new RuntimeException("runner agent name is blank");
        }

        List<BasePlugin> pluginList = new java.util.ArrayList<>();

        List<String> plugins = armoryCommandEntity.getAiAgentConfigTableVO().getModule().getRunner().getPlugins();
        if(plugins != null && !plugins.isEmpty()){
            for(String plugin : plugins){
                BasePlugin pluginBean = getBeanByName(plugin);
                pluginList.add(pluginBean);
            }

        }


        BaseAgent runnerAgent = dynamicContext.getAgentGroup().get(runnerAgentName);
        InMemoryRunner runner = new InMemoryRunner(runnerAgent, agent.getAgentId(),pluginList);
        return runner;
    }
}
