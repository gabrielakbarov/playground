package AI;

import AI.Config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chat {

    private static final Logger log = LoggerFactory.getLogger(Chat.class);

    private final OpenAIClient client;

    public Chat(String apiKey, String baseUrl) {
        this.client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();
    }

    public String chat(String input) {
        try {
            ChatCompletionCreateParams params =
                    ChatCompletionCreateParams.builder()
                            .model(Config.AI_MODEL)
                            .addUserMessage(input)
                            .maxCompletionTokens(Config.AI_MAX_TOKENS)
                            .temperature(Config.AI_TEMPERATURE)
                            .build();

            ChatCompletion completion =
                    client.chat().completions().create(params);

            String response = completion.choices().getFirst().message().content().get();

            log.info("AI Response: {}", response);
            return (response);
        } catch (Exception e) {
            log.error("Fehler beim Aufruf des AI-Services: {}", e.getMessage());
        }
        return null;
    }
}