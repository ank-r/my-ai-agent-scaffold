package org.ryc.ai.domain.agent.service.armory;

import org.apache.commons.collections4.CollectionUtils;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.ryc.ai.domain.agent.service.armory.node.RootNode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ArmoryAgentServiceImpl
 * @Description
 * @Author admin
 * @Time 2026/7/12 16:27
 * @Version 1.0
 */
@Service
public class ArmoryAgentServiceImpl implements ArmoryAgentService{

    @Resource
    private DefaultArmoryFactory defaultArmoryFactory;

    @Override
    public void acceptArmoryAgents(List<AiAgentConfigTableVO> tables) throws Exception {


        if(CollectionUtils.isEmpty(tables)){
            return;
        }

        RootNode defaultArmoryNode = defaultArmoryFactory.getDefaultArmoryNode();

        for (AiAgentConfigTableVO table : tables){
            ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
            armoryCommandEntity.setAiAgentConfigTableVO(table);
            defaultArmoryNode.apply(armoryCommandEntity,new DefaultArmoryFactory.DynamicContext());
        }

    }
}
