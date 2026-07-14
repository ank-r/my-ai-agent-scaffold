package org.ryc.ai.domain.agent.service.armory.matter.mcp.client.factory;

import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.McpClientLoad;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.client.impl.LocalMcpClientLoadImpl;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.client.impl.SeeMcpClientLoadImpl;
import org.ryc.ai.domain.agent.service.armory.matter.mcp.client.impl.StdioMcpClientLoadImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName MacpLoadFactory
 * @Description
 * @Author admin
 * @Time 2026/7/13 23:11
 * @Version 1.0
 */
@Service
public class McpLoadFactory {

    @Resource
    private LocalMcpClientLoadImpl localMcpClientLoad;

    @Resource
    private SeeMcpClientLoadImpl seeMcpClientLoad;

    @Resource
    private StdioMcpClientLoadImpl stdioMcpClientLoad;


    public McpClientLoad getMcpClientLoad(AiAgentConfigTableVO.Module.ChatModel.ToolMcp toolMcp ) {


        if (null != toolMcp.getLocalMcp()) return localMcpClientLoad;
        if (null != toolMcp.getSse()) return seeMcpClientLoad;
        if (null != toolMcp.getStdio()) return stdioMcpClientLoad;
        throw new RuntimeException("ToolMcp type not found");

    }

}
