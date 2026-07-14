package org.ryc.ai.domain.agent.service.armory.matter.skills;

import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.springframework.ai.tool.ToolCallback;

/**
 * @ClassName LoadSkillsService
 * @Description
 * @Author admin
 * @Time 2026/7/14 15:41
 * @Version 1.0
 */
public interface LoadSkillsService {

    ToolCallback[] getSkillsToolCallback(AiAgentConfigTableVO.Module.ChatModel.Skill skill);
}
