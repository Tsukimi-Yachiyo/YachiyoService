package com.yachiyo.service.Impl;

import com.yachiyo.Config.SecuritySafeToolConfig;
import com.yachiyo.Utils.JwtUtils;
import com.yachiyo.entity.User;
import com.yachiyo.result.Result;
import com.yachiyo.service.AdminService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openaisdk.OpenAiSdkChatModel;
import org.springframework.ai.openaisdk.OpenAiSdkChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import static com.baomidou.mybatisplus.extension.spi.SpringCompatibleSet.applicationContext;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SecuritySafeToolConfig securitySafeToolConfig;

    @Override
    public Result<String> Login(User user) {
        if (user.getName().equals("admin")) {
            if (user.getPassword().equals(securitySafeToolConfig.md5("123456"))) {
                return Result.success(jwtUtils.generateToken(0L, "admin", String.valueOf(securitySafeToolConfig.getStatusSafeCode())));
            }
        }
        return Result.error("400", "登录失败", "登录失败");
    }

    @Override
    public Result<Long> GetRemainingToken() {
        return Result.success(0L);
    }

    @Value("${spring.ai.openai-sdk.chat.options.temperature}")
    private String temperature;

    @Value("${spring.ai.openai-sdk.chat.options.top-p}")
    private String topP;

    @Autowired
    private ChatMemory chatMemory;

    @Autowired
    private Advisor retrievalAugmentationAdvisor;

    @Override
    public Result<Void> ChangeApiKey(String apiKey, String model) {
        OpenAiSdkChatOptions openAiSdkChatOptions = new OpenAiSdkChatOptions();
        openAiSdkChatOptions.setApiKey(apiKey);
        openAiSdkChatOptions.setModel(model);
        openAiSdkChatOptions.setTemperature(Double.valueOf(temperature));
        openAiSdkChatOptions.setTopP(Double.valueOf(topP));
        OpenAiSdkChatModel openAiSdkChatModel = new OpenAiSdkChatModel(openAiSdkChatOptions);

        ChatClient chatClient = ChatClient.builder(openAiSdkChatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        retrievalAugmentationAdvisor
                )
                .build();

        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) context.getBeanFactory();

        // 1. 销毁旧 Bean
        beanFactory.destroySingleton("ChatModel");
        // 2. 注册新 Bean
        beanFactory.registerSingleton("ChatModel", chatClient);
        return Result.success(null);
    }
}
