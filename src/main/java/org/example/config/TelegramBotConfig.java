package org.example.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@PropertySource("classpath:app.properties")
public class TelegramBotConfig {

    @Value("${telegrambot.webHookPath}")
    String webHookPath;
    @Value("${telegrambot.botToken}")
    String botToken;
    @Value("${telegrambot.userName}")
    String userName;
}
