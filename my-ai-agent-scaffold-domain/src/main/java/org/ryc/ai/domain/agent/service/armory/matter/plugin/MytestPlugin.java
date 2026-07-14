package org.ryc.ai.domain.agent.service.armory.matter.plugin;

import com.google.adk.agents.CallbackContext;
import com.google.adk.agents.InvocationContext;
import com.google.adk.models.LlmResponse;
import com.google.adk.plugins.BasePlugin;
import com.google.genai.types.Content;
import io.reactivex.rxjava3.core.Maybe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName MytestPlugin
 * @Description
 * @Author admin
 * @Time 2026/7/14 9:51
 * @Version 1.0
 */
@Slf4j
@Service("mytestPlugin")
public class MytestPlugin extends BasePlugin {

    public MytestPlugin(String name) {
        super(name);
    }


    public MytestPlugin() {
        super("mytestPlugin");
    }


    @Override
    public Maybe<Content> onUserMessageCallback(InvocationContext invocationContext, Content userMessage) {

        log.info("onUserMessage: {}",userMessage.text());
        return super.onUserMessageCallback(invocationContext, userMessage);
    }


    @Override
    public Maybe<LlmResponse> afterModelCallback(CallbackContext callbackContext, LlmResponse llmResponse) {



        return super.afterModelCallback(callbackContext, llmResponse);
    }
}
