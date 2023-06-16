package org.example.config;

import org.example.bots.HerokuBot;
import org.example.bots.TgFacade;
import org.example.dao.UserDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class AppConfig {
    private final TelegramBotConfig telegramBotConfig;
    public AppConfig(TelegramBotConfig telegramBotConfig){
        this.telegramBotConfig = telegramBotConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramBotConfig.getWebHookPath()).build();
    }

    @Bean
    public HerokuBot springWebHookBot(SetWebhook setWebhook, TgFacade tgFacade) {
        HerokuBot bot = new HerokuBot(tgFacade, setWebhook);
        bot.setBotToken(telegramBotConfig.getBotToken());
        bot.setBotPath(telegramBotConfig.getWebHookPath());
        bot.setBotUsername(telegramBotConfig.getUserName());
        return bot;
    }

}
