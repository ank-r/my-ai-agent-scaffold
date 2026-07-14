package org.ryc.ai.domain.agent.service.armory.matter.skills.impl;

import lombok.extern.slf4j.Slf4j;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.service.armory.matter.skills.LoadSkillsService;
import org.springaicommunity.agent.tools.SkillsTool;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DefaultLoadSkillsService
 * @Description
 * @Author admin
 * @Time 2026/7/14 15:47
 * @Version 1.0
 */
@Service
@Slf4j
public class DefaultLoadSkillsService implements LoadSkillsService {

    @Override
    public ToolCallback[] getSkillsToolCallback(AiAgentConfigTableVO.Module.ChatModel.Skill skill) {
        String type = skill.getType();
        String path = skill.getPath();

        log.info("开始装载skills type： {}， path: {}" ,type ,path);

        List<ToolCallback> toolCallbackList = new ArrayList<>();

        if ("directory".equals(type)){
            ToolCallback toolCallback = SkillsTool.builder()
                    .addSkillsDirectory(path)
                    .build();
            toolCallbackList.add(toolCallback);
        }

        if ("resource".equals(type)){
            ToolCallback toolCallback = SkillsTool.builder()
                    .addSkillsResource(new ClassPathResource(path))
                    .build();
            toolCallbackList.add(toolCallback);
        }

        return toolCallbackList.toArray(new ToolCallback[0]);



    }
}
