package org.ryc.ai.test.api.agent;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.model.valobj.properties.AiAgentAutoConfigProperties;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.ryc.ai.domain.agent.service.armory.node.RootNode;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName AramoryTest
 * @Description
 * @Author admin
 * @Time 2026/7/12 14:14
 * @Version 1.0
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AramoryTest {

    @Resource
    private DefaultArmoryFactory defaultArmoryFactory;

    @Resource
    private AiAgentAutoConfigProperties aiAgentAutoConfigProperties;

    @Test
    public void testGetDefaultArmoryNode() {

        RootNode defaultArmoryNode = defaultArmoryFactory.getDefaultArmoryNode();


        Map<String, AiAgentConfigTableVO> tables = aiAgentAutoConfigProperties.getTables();
        tables.keySet().forEach(key -> {

            log.info("开始加载agent {}", key);
            AiAgentConfigTableVO aiAgentConfigTableVO = tables.get(key);
            ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
            armoryCommandEntity.setAiAgentConfigTableVO(aiAgentConfigTableVO);
            AiAgentRegisterVO apply = null;
            try {
                apply = defaultArmoryNode.apply(armoryCommandEntity, new DefaultArmoryFactory.DynamicContext());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ChatModel chatModel = apply.getDynamicContext().getChatModel();

            String response = chatModel.call("你有那些工具可以用");
            log.info("agent {}- 的回答： {}", key, response);


        });



    }
}
