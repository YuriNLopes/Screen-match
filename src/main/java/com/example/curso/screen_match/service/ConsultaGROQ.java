package com.example.curso.screen_match.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import io.github.cdimascio.dotenv.Dotenv;

public class ConsultaGROQ {

    private static final Dotenv dotenv = Dotenv.load();

    private static final OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey(dotenv.get("GROQ_API_KEY"))
            .baseUrl("https://api.groq.com/openai/v1")
            .build();

    public static String obterTraducao(String texto) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model("openai/gpt-oss-120b")
                .addUserMessage("Traduza para o português o texto a seguir: " + texto)
                .build();

        ChatCompletion completion = client.chat().completions().create(params);

        System.out.println(completion.choices().stream()
                .flatMap(choice -> choice.message().content().stream())
                .findFirst()
                .orElse(""));

        return completion.choices().stream()
                .flatMap(choice -> choice.message().content().stream())
                .findFirst()
                .orElse("");
    }
}
