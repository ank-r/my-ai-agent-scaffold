package org.ryc.ai.api.dto;

import lombok.Data;

@Data
public class ChatRequestDTO {

    /**
     * 大模型id
     */
    private String agentId;
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 会话ID
     */
    private String sessionId;
    /**
     * 用户消息
     */
    private String message;

}
