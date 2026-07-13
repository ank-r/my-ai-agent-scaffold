package org.ryc.ai.domain.agent.model.valobj;

import com.google.adk.runner.InMemoryRunner;
import lombok.*;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;

/**
 *
 * agent注册值对象
 * @ClassName AiAgentRegisterVO
 * @Description
 * @Author admin
 * @Time 2026/7/12 9:15
 * @Version 1.0
 */
@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAgentRegisterVO {

    private DefaultArmoryFactory.DynamicContext dynamicContext;



    /**
     * 智能体名称
     */
    private String appName;

    /**
     * 智能体ID
     */
    private String agentId;

    /**
     * 智能体名称
     */
    private String agentName;

    /**
     * 智能体描述
     */
    private String agentDesc;

    /**
     * 智能体执行对象
     */
    private InMemoryRunner runner;
}
