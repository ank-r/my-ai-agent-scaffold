package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.AbstractMultiThreadStrategyRouter;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName AbstractArmorySupport
 * @Description
 * @Author admin
 * @Time 2026/7/11 20:49
 * @Version 1.0
 */
public abstract class AbstractArmorySupport extends AbstractMultiThreadStrategyRouter<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> {

    @Override
    protected void multiThread(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws ExecutionException, InterruptedException, TimeoutException {

    }


}
