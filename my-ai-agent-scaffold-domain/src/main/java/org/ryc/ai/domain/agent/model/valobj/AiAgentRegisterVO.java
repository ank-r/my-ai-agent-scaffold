package org.ryc.ai.domain.agent.model.valobj;

import lombok.Data;
import lombok.Getter;
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
public class AiAgentRegisterVO {

    private DefaultArmoryFactory.DynamicContext dynamicContext;
}
