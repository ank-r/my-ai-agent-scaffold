package org.ryc.ai.domain.agent.service.armory.matter.mcp.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.McpClientLoad;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName LocalMcpClientLoadImpl
 * @Description
 * @Author admin
 * @Time 2026/7/13 22:19
 * @Version 1.0
 */
@Service
@Slf4j
public class LocalMcpClientLoadImpl implements McpClientLoad {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public ToolCallback[] getToolCallbacks(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp) {

        AiAgentConfigTableVO.Module.ChatModel.ToolMcp.LocalMcp localMcp = toolMcp.getLocalMcp();

        if(localMcp != null){

            String name = localMcp.getName();

            ToolCallbackProvider toolCallback = applicationContext.getBean(name, ToolCallbackProvider.class);

            if (toolCallback == null){
                throw new RuntimeException("ToolCallbackProvider not found: " + name);
            }

            return toolCallback.getToolCallbacks();

        }


        return new ToolCallback[0];
    }
}
