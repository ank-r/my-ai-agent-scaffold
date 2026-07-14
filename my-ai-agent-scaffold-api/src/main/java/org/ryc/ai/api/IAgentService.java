package org.ryc.ai.api;

import org.ryc.ai.api.dto.*;
import org.ryc.ai.api.response.Response;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.util.List;

/**
 * @ClassName IAgentService
 * @Description
 * @Author admin
 * @Time 2026/7/14 14:28
 * @Version 1.0
 */
public interface IAgentService {

    List<AiAgentConfigResponseDTO> getAllAgentConfigs();

    Response<ChatResponseDTO> chat(ChatRequestDTO chatRequestDTO);

    Response<CreateSessionResponseDTO> createSession(CreateSessionRequestDTO createSessionRequestDTO);

    ResponseBodyEmitter chatStream(ChatRequestDTO chatRequestDTO);

}
