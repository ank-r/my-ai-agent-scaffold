package org.ryc.ai.domain.agent.service.armory.node;

import cn.bugstack.wrench.design.framework.tree.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ryc.ai.domain.agent.model.entity.ArmoryCommandEntity;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.factory.DefaultArmoryFactory;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName ApiNOde
 * @Description
 * @Author admin
 * @Time 2026/7/12 9:20
 * @Version 1.0
 */
@Service
@Slf4j
public class ApiNode extends AbstractArmorySupport{

    @Resource
    private ChatModelNode chatModelNode;

    @Override
    protected AiAgentRegisterVO doApply(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {

        log.info("Ai Agent 装配操作 - AiApiNode");

        AiAgentConfigTableVO aiAgentConfigTableVO = armoryCommandEntity.getAiAgentConfigTableVO();
        AiAgentConfigTableVO.Module.AiApi aiApi = aiAgentConfigTableVO.getModule().getAiApi();


        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl(aiApi.getBaseUrl())
                .apiKey(aiApi.getApiKey())
                .completionsPath(StringUtils.isNotBlank(aiApi.getCompletionsPath()) ? aiApi.getCompletionsPath() : "v1/chat/completions")
                .embeddingsPath(StringUtils.isNotBlank(aiApi.getEmbeddingsPath()) ? aiApi.getEmbeddingsPath() :  "v1/embeddings")
                .build();

        dynamicContext.setOpenAiApi(openAiApi);

        return router(armoryCommandEntity, dynamicContext);
    }

    @Override
    public StrategyHandler<ArmoryCommandEntity, DefaultArmoryFactory.DynamicContext, AiAgentRegisterVO> get(ArmoryCommandEntity armoryCommandEntity, DefaultArmoryFactory.DynamicContext dynamicContext) throws Exception {
        return chatModelNode;
    }
}
