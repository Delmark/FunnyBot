package ru.delmark.FunnyBot.Config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramConfig {

    @Value("${bot.token}")
    private String token;

    @Bean
    TelegramBot telegramBot() {
        return new TelegramBot(token);
    }
}
