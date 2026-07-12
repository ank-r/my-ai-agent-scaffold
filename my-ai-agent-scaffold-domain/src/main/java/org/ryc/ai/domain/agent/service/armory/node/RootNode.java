package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName RootNode
 * @Description
 * @Author admin
 * @Time 2026/7/12 9:17
 * @Version 1.0
 */
@Service
public class RootNode extends AbstractArmorySupport{


    @Resource
    private ApiNode apiNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {


        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        return apiNode;
    }
}
