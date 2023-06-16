package org.example.controllers;

import org.example.bots.HerokuBot;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final HerokuBot herokuBot;
    public WebHookController(HerokuBot herokuBot){
        this.herokuBot = herokuBot;
    }
    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        System.out.println("!!!");
        return herokuBot.onWebhookUpdateReceived(update);

    }


    @GetMapping
    public ResponseEntity get(){
        return ResponseEntity.ok().build();
    }
}
