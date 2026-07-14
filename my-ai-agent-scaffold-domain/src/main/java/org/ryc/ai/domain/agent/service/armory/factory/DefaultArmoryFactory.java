package org.ryc.ai.domain.agent.service.armory.factory;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.runner.InMemoryRunner;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.ryc.ai.domain.agent.model.valobj.AiAgentConfigTableVO;
import org.ryc.ai.domain.agent.model.valobj.AiAgentRegisterVO;
import org.ryc.ai.domain.agent.service.armory.node.RootNode;
import org.ryc.ai.types.enums.ResponseCode;
import org.ryc.ai.types.exception.AppException;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @ClassName DefaultArmoryFactory
 * @Description
 * @Author admin
 * @Time 2026/7/12 9:10
 * @Version 1.0
 */
@Service
public class DefaultArmoryFactory {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RootNode rootNode;

    public RootNode getDefaultArmoryNode() {
        return rootNode;
    }


    public AiAgentRegisterVO getAiAgentRegisterVO(String clientId) {

        AiAgentRegisterVO applicationContextBean = applicationContext.getBean(clientId, AiAgentRegisterVO.class);
        if(applicationContextBean == null){
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER);
        }

        return applicationContextBean;
    }

    public InMemoryRunner getInMemoryRunner(String clientId) {
        return getAiAgentRegisterVO(clientId).getRunner();
    }


    @Data
    public static class DynamicContext {

        private OpenAiApi openAiApi;

        private ChatModel chatModel;

        private Map<String, BaseAgent> agentGroup = new HashMap<>();

        private SequentialAgent sequentialAgent;



        /**
         * 当前处理中的流程节点
         */
        AiAgentConfigTableVO.Module.AgentWorkflow currentAgentWorkflow;

        private AtomicInteger agentWorkflowIndex = new AtomicInteger(0);



        private Map<String,Object> dataObjects =  new HashMap<>();

        public <T> T getValue(String key) {
            return (T)dataObjects.get(key);
        }

        public <T> void setValue(String key, T value ) {
            this.dataObjects.put(key, value);
        }

        public List<BaseAgent> getAgentFromAgentGroup(List<String> agentNames) {
            if(CollectionUtils.isEmpty(agentNames) || agentGroup == null || agentGroup.isEmpty()){
                return  Collections.emptyList();
            }


            return agentNames.stream().map(agentGroup::get).filter(agent -> agent != null).collect(Collectors.toList());

        }

        public void addAgentWorkflowIndex(){
            this.agentWorkflowIndex.incrementAndGet();
        }

        public int getCurrentAgentWorkflowIndex(){
            return this.agentWorkflowIndex.get();
        }
    }


}
