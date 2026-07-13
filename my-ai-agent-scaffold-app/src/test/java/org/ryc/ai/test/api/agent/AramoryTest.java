package org.ryc.ai.test.api.agent;

import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.model.valobj.properties.AiAgentAutoConfigProperties;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

    @Resource
    private ApplicationContext applicationContext;

    @Test
    public void testGetDefaultArmoryNode() {

        // RootNode defaultArmoryNode = defaultArmoryFactory.getDefaultArmoryNode();
        //
        //
        // Map<String, AiAgentConfigTableVO> tables = aiAgentAutoConfigProperties.getTables();
        // String key = "testAgent";
        //
        //     log.info("开始加载agent {}", key);
        //     AiAgentConfigTableVO aiAgentConfigTableVO = tables.get(key);
        //     ArmoryCommandEntity armoryCommandEntity = new ArmoryCommandEntity();
        //     armoryCommandEntity.setAiAgentConfigTableVO(aiAgentConfigTableVO);
        //     DefaultArmoryFactory.DynamicContext dynamicContext = new DefaultArmoryFactory.DynamicContext();
        //     AiAgentRegisterVO aiAgentRegisterVO = new AiAgentRegisterVO();
        //     try {
        //         aiAgentRegisterVO =    defaultArmoryNode.apply(armoryCommandEntity, dynamicContext);
        //     } catch (Exception e) {
        //         throw new RuntimeException(e);
        //     }
        //
        //     ChatModel chatModel = dynamicContext.getChatModel();
        //
        //     String response = chatModel.call("你有那些工具可以用");
        //     log.info("agent {}- 的回答： {}", key, response);
        //



        AiAgentRegisterVO  aiAgentRegisterVO = applicationContext.getBean("100001", AiAgentRegisterVO.class);


        // Create an InMemoryRunner
        InMemoryRunner runner = aiAgentRegisterVO.getRunner();
        // InMemoryRunner automatically creates a session service. Create a session using the service
        Session session = runner.sessionService().createSession(aiAgentRegisterVO.getAgentId(), "user-1234").blockingGet();
        Content userMessage = Content.fromParts(Part.fromText("Write a Java function to calculate the factorial of a number.  to path E:\\Study\\xaiofuge\\my-ai-agent-scaffold\\my-ai-agent-scaffold-app\\data"));

        // Run the agent
        Flowable<Event> eventStream = runner.runAsync("user-1234", session.id(), userMessage);

        // Stream event response
        eventStream.blockingForEach(
                event -> {
                    if (event.finalResponse()) {
                        System.out.println(event.stringifyContent());
                    }
                });


    }

    @Test
    public void testOnlyOneAgent(){

        AiAgentRegisterVO aiAgentRegisterVO = applicationContext.getBean("100002", AiAgentRegisterVO.class);
        InMemoryRunner runner = aiAgentRegisterVO.getRunner();

        Session session = runner.sessionService().createSession(aiAgentRegisterVO.getAgentId(), "user-1234").blockingGet();

        String message = "写一个关于如何Google ADK使用介绍的ppt 文件最终文件放在 path E:\\Study\\xaiofuge\\my-ai-agent-scaffold\\my-ai-agent-scaffold-app\\data";
        Flowable<Event> eventFlowable = runner.runAsync("user-1234", session.id(), Content.fromParts(Part.fromText(message)));


        eventFlowable.blockingForEach(
                event -> {
                    if (event.finalResponse()) {
                        System.out.println(event.stringifyContent());
                    }
                });

    }
}
