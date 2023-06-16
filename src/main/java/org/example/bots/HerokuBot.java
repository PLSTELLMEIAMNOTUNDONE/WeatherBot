package org.example.bots;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.example.dao.UserDAO;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HerokuBot extends SpringWebhookBot {

    String botUsername;
    String botPath;
    String botToken;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        return tgFacade.handleUpdate(update);
    }
    public HerokuBot(TgFacade tg, DefaultBotOptions defaultOptions, SetWebhook setWebhook){
        super(defaultOptions,setWebhook);
        this.tgFacade = tg;

    }
    @PostConstruct
    public void inf(){
        System.out.println(this.getBotPath());
        System.out.println(this.getBotToken());
        System.out.println(this.getBotUsername());
    }
    public HerokuBot(TgFacade tg, SetWebhook setWebhook){
        super(setWebhook);
        this.tgFacade = tg;

    }


   TgFacade tgFacade;
}
