package org.ryc.ai.domain.agent.service.chat;

import com.google.adk.events.Event;
import io.reactivex.rxjava3.core.Flowable;
import org.ryc.ai.domain.agent.model.entity.ChatCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;

import java.util.List;

/**
 * @ClassName ChatService
 * @Description
 * @Author admin
 * @Time 2026/7/14 10:44
 * @Version 1.0
 */
public interface ChatService {

     String createSession(String agentId , String userId);

     List<AiAgentConfigTableVO.Agent> getAllAgent();

     List<String> handleMessage(String agentId, String userId, String message, String sessionId);

    List<String> handleMessage(String agentId, String userId, String message);

    Flowable<Event> handleMessageStream(String agentId, String userId, String message, String sessionId);

    Flowable<Event> handleMessageStream(String agentId, String userId, String message);


    List<String> handleMessage(ChatCommandEntity chatCommandEntity);





}
