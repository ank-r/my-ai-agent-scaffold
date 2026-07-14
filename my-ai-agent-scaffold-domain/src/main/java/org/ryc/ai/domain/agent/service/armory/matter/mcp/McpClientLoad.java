package org.ryc.ai.domain.agent.service.armory.matter.mcp;

import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.springframework.ai.tool.ToolCallback;

/**
 * @ClassName McpClientLoad
 * @Description
 * @Author admin
 * @Time 2026/7/13 22:06
 * @Version 1.0
 */
public interface McpClientLoad {

    ToolCallback[] getToolCallbacks(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp);
}


