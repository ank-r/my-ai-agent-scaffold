package org.ryc.ai.domain.agent.service.armory;

import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;

import java.util.List;

/**
 * @ClassName ArmoryAgentService
 * @Description
 * @Author admin
 * @Time 2026/7/11 20:47
 * @Version 1.0
 */
public interface ArmoryAgentService {

    void acceptArmoryAgents(List<AiAgentConfigTableVO> tables) throws Exception;

}
