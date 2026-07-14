package org.ryc.ai.domain.agent.service.chat.impl;

import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ryc.ai.domain.agent.model.entity.ChatCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.model.valobj.properties.AiAgentAutoConfigProperties;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.ryc.ai.domain.agent.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ChatServiceImpl
 * @Description
 * @Author admin
 * @Time 2026/7/14 11:02
 * @Version 1.0

 */

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    @Autowired
    private DefaultArmoryFactory defaultArmoryFactory;

    @Autowired
    private AiAgentAutoConfigProperties aiAgentAutoConfigProperties;

    private final static java.util.Map<String, String> runnerSession = new ConcurrentHashMap<>();

    @Override
    public String createSession(String agentId, String userId) {

        AiAgentRegisterVO aiAgentRegisterVO = defaultArmoryFactory.getAiAgentRegisterVO(agentId);

        InMemoryRunner runner = aiAgentRegisterVO.getRunner();

        return runnerSession.computeIfAbsent(userId, uid -> {
            Session session = runner.sessionService().createSession(agentId, uid)
                    .blockingGet();
            return session.id();
        });
    }

    @Override
    public List<AiAgentConfigTableVO.Agent> getAllAgent() {

        List<AiAgentConfigTableVO.Agent> agents = new ArrayList<>();

        Map<String, AiAgentConfigTableVO> tables = aiAgentAutoConfigProperties.getTables();

        tables.forEach((key, value) -> {
            AiAgentConfigTableVO aiAgentConfigTableVO = tables.get(key);
            if(aiAgentConfigTableVO.getAgent() != null){
                agents.add(aiAgentConfigTableVO.getAgent());
            }
        });
        return agents;
    }

    @Override
    public List<String> handleMessage(String agentId, String userId, String message, String sessionId) {

        InMemoryRunner inMemoryRunner = defaultArmoryFactory.getInMemoryRunner(agentId);


        Content content =Content.fromParts(Part.fromText(message));

        List<String> response = new ArrayList<>();
        Flowable<Event> eventFlowable = inMemoryRunner.runAsync(userId, sessionId, content);

        eventFlowable.blockingForEach(re -> response.add(re.stringifyContent()));

        return response;
    }

    @Override
    public List<String> handleMessage(String agentId, String userId, String message) {
        String session = createSession(agentId, userId);

        return handleMessage(agentId, userId, message, session);


    }

    @Override
    public Flowable<Event> handleMessageStream(String agentId, String userId, String message, String sessionId) {


        InMemoryRunner inMemoryRunner = defaultArmoryFactory.getInMemoryRunner(agentId);


        Content content =Content.fromParts(Part.fromText(message));

        List<String> response = new ArrayList<>();
        Flowable<Event> eventFlowable = inMemoryRunner.runAsync(userId, sessionId, content);


        return eventFlowable;
    }

    @Override
    public Flowable<Event> handleMessageStream(String agentId, String userId, String message) {
        String session = createSession(agentId, userId);

        return handleMessageStream(agentId, userId, message, session);
    }

    @Override
    public List<String> handleMessage(ChatCommandEntity chatCommandEntity) {

        AiAgentRegisterVO aiAgentRegisterVO = defaultArmoryFactory.getAiAgentRegisterVO(chatCommandEntity.getAgentId());



        List<Part> parts = new ArrayList<>();

        List<ChatCommandEntity.Content.Text> texts = chatCommandEntity.getTexts();
        if (null != texts && !texts.isEmpty()) {
            for (ChatCommandEntity.Content.Text text : texts) {
                parts.add(Part.fromText(text.getMessage()));
            }
        }

        List<ChatCommandEntity.Content.File> files = chatCommandEntity.getFiles();
        if (null != files && !files.isEmpty()) {
            for (ChatCommandEntity.Content.File file : files) {
                parts.add(Part.fromUri(file.getFileUri(), file.getMimeType()));
            }
        }

        List<ChatCommandEntity.Content.InlineData> inlineDatas = chatCommandEntity.getInlineDatas();
        if (null != inlineDatas && !inlineDatas.isEmpty()) {
            for (ChatCommandEntity.Content.InlineData inlineData : inlineDatas) {
                parts.add(Part.fromBytes(inlineData.getBytes(), inlineData.getMimeType()));
            }
        }

        Content content = Content.builder().role("user").parts(parts).build();

        // 获取运行体
        InMemoryRunner runner = aiAgentRegisterVO.getRunner();
        if(StringUtils.isBlank(chatCommandEntity.getSessionId())){
            chatCommandEntity.setSessionId(createSession(chatCommandEntity.getAgentId(), chatCommandEntity.getUserId()));
        }

        Flowable<Event> events = runner.runAsync(chatCommandEntity.getUserId(), chatCommandEntity.getSessionId(), content);

        List<String> outputs = new ArrayList<>();
        events.blockingForEach(event -> outputs.add(event.stringifyContent()));

        return outputs;
    }
}
