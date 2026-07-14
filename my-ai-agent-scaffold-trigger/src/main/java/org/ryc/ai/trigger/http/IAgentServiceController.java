package org.ryc.ai.trigger.http;

import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.api.IAgentService;
import org.ryc.ai.api.dto.*;
import org.ryc.ai.api.response.Response;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.service.chat.ChatService;
import org.ryc.ai.types.enums.ResponseCode;
import org.ryc.ai.types.exception.AppException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName IAgentServiceController
 * @Description
 * @Author admin
 * @Time 2026/7/14 14:33
 * @Version 1.0
 */

@Slf4j
@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "*")
public class IAgentServiceController implements IAgentService {

    @Resource
    private ChatService chatService;

    /**
     * 当前可选的大模型列表
     * @return
     */
    @GetMapping("/agent/configs")
    @Override
    public List<AiAgentConfigResponseDTO> getAllAgentConfigs() {
        List<AiAgentConfigTableVO.Agent> agents = chatService.getAllAgent();
        List<AiAgentConfigResponseDTO> result = new ArrayList<>();
        if (agents != null) {
            for (AiAgentConfigTableVO.Agent agent : agents) {
                AiAgentConfigResponseDTO dto = new AiAgentConfigResponseDTO();
                dto.setAgentId(agent.getAgentId());
                dto.setAgentName(agent.getAgentName());
                dto.setAgentDesc(agent.getAgentDesc());
                result.add(dto);
            }
        }
        return result;
    }

    /**
     * 智能体对话
     * @param chatRequestDTO
     * @return
     */
    @PostMapping("/agent/chat")
    @Override
    public Response<ChatResponseDTO> chat(@RequestBody ChatRequestDTO chatRequestDTO) {

        try {
            List<String> messages = chatService.handleMessage(chatRequestDTO.getAgentId(), chatRequestDTO.getUserId(), chatRequestDTO.getMessage(), chatRequestDTO.getSessionId());

            ChatResponseDTO responseDTO = new ChatResponseDTO();
            responseDTO.setContent(String.join("\n", messages));
            return Response.<ChatResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        }
        catch (AppException appException){
            log.error("智能体对话败 agentId:{} userId:{}", chatRequestDTO.getAgentId(), chatRequestDTO.getUserId(), appException);
            return Response.<ChatResponseDTO>builder()
                    .code(appException.getCode())
                    .info(appException.getInfo())
                    .build();
        }
        catch (Exception e) {
            log.error("智能体对话败 agentId:{} userId:{}", chatRequestDTO.getAgentId(), chatRequestDTO.getUserId(), e);
            return Response.<ChatResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }


    }

    /**
     * 会话创建 实际聊天之前要创建聊天会话id
     * @param createSessionRequestDTO
     * @return
     */
    @PostMapping("/agent/session")
    @Override
    public Response<CreateSessionResponseDTO> createSession(@RequestBody CreateSessionRequestDTO createSessionRequestDTO) {


        try {
            String sessionID = chatService.createSession(createSessionRequestDTO.getAgentId(), createSessionRequestDTO.getUserId());
            CreateSessionResponseDTO responseDTO = new CreateSessionResponseDTO();
            responseDTO.setSessionId(sessionID);
            return Response.<CreateSessionResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(responseDTO)
                    .build();
        }
        catch (AppException appException){
            log.error("智能体对话败 agentId:{} userId:{}", createSessionRequestDTO.getAgentId(), createSessionRequestDTO.getUserId(), appException);
            return Response.<CreateSessionResponseDTO>builder()
                    .code(appException.getCode())
                    .info(appException.getInfo())
                    .build();
        }
        catch (Exception e) {
            log.error("智能体对话败 agentId:{} userId:{}", createSessionRequestDTO.getAgentId(), createSessionRequestDTO.getUserId(), e);
            return Response.<CreateSessionResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }


    }

    /**
     * 智能体对话流式
     * @param chatRequestDTO
     * @return
     */
    @PostMapping("/agent/chat/stream")
    @Override
    public ResponseBodyEmitter chatStream(@RequestBody ChatRequestDTO chatRequestDTO) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        try {
            chatService.handleMessageStream(chatRequestDTO.getAgentId(), chatRequestDTO.getUserId(), chatRequestDTO.getMessage(), chatRequestDTO.getSessionId())
                    .subscribe(event -> {

                                try {
                                    emitter.send(event.stringifyContent());
                                }catch (Exception exception){
                                    log.error("智能体对话败 agentId:{} userId:{}", chatRequestDTO.getAgentId(), chatRequestDTO.getUserId(), exception);
                                    emitter.completeWithError(exception);
                                }
                            },
                            emitter::completeWithError,
                            emitter::complete);

        }catch (Exception exception){
            log.error("智能体对话败 agentId:{} userId:{}", chatRequestDTO.getAgentId(), chatRequestDTO.getUserId(), exception);
            emitter.completeWithError(exception);
        }


        return emitter;
    }
}
