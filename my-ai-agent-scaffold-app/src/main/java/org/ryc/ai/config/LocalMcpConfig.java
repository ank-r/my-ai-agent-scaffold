package org.ryc.ai.config;

import org.ryc.ai.domain.agent.service.armory.matter.mcp.server.WeatherService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName LocalMcpConfig
 * @Description
 * @Author admin
 * @Time 2026/7/13 23:01
 * @Version 1.0
 */
@Configuration
public class LocalMcpConfig {


    @Bean("weatherServiceMcp")
    public ToolCallbackProvider weatherServiceMcp(@Autowired WeatherService weatherService) {
        // 配置本地mcp
        return MethodToolCallbackProvider.builder().toolObjects(weatherService).build();
    }
}
