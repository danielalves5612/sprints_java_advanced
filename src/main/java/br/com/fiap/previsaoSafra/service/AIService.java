package br.com.fiap.previsaoSafra.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.azure.ai.openai.models.ChatCompletions;

import java.util.Collections;

@Service
public class AIService {

    private final OpenAIClient openAIClient;
    private final String modelName;

    public AIService(@Value("${azure.openai.endpoint}") String endpoint,
                     @Value("${azure.openai.api-key}") String apiKey,
                     @Value("${azure.openai.model-name}") String modelName) { 
        this.openAIClient = new OpenAIClientBuilder()
            .endpoint(endpoint)
            .credential(new AzureKeyCredential(apiKey))
            .buildClient();
        this.modelName = modelName; 
    }

    public String processarPergunta(String pergunta) {
        
        ChatMessage userMessage = new ChatMessage(ChatRole.USER, pergunta);

        ChatCompletionsOptions options = new ChatCompletionsOptions(Collections.singletonList(userMessage));
        options.setMaxTokens(500);  

        ChatCompletions chatCompletions = openAIClient.getChatCompletions(modelName, options);

        return chatCompletions.getChoices().get(0).getMessage().getContent().trim();
    }
}
