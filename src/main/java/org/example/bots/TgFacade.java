package org.example.bots;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.commands.*;

import org.example.dao.UserDAO;
import org.example.models.User;
import org.example.models.WeatherModel;
import org.example.service.UserService;
import org.example.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TgFacade {
    @Autowired
    UserService userService;
    @Autowired
    WeatherService weatherService;

    States state = States.DEFAULT;

    /*   public BotApiMethod<?> handleStart(UserDAO userDAO, Update update) {
           this.userDAO = userDAO;
           Message message = update.getMessage();
           SendMessage sendMessage = new SendMessage();
           sendMessage.setChatId(String.valueOf(message.getChatId()));

           File file = new File("user" + String.valueOf(message.getChatId()) + ".txt");

           User user = User.builder().file(file).chatId(String.valueOf(message.getChatId())).build();
           userDAO.addUser(String.valueOf(message.getChatId()),user);
           sendMessage.setText("hi");
           return sendMessage;


       }
   */
    public BotApiMethod<?> handleUpdate(Update update) {

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String chatId = String.valueOf(callbackQuery.getMessage().getChatId());

            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(chatId);
            User user = userService.getOrCreateUser(chatId);

            if (callbackQuery.getData().equals(Queries.WEATHER.toString())) {
                String city = user.getCity();
                WeatherModel weather = weatherService.getWeatherInCity(city);
                sendMessage.setText("Температура - " + weather.getTemp() + "\n" +
                        "Ощущается как - " + weather.getFeels_like() + "\n" +
                        "Давление - " + weather.getPressure() + "\n");
                return sendMessage;

            }
            if (callbackQuery.getData().equals(Queries.SET_CITY.toString())) {
                state = States.CITY_WAITING;
                sendMessage.setText("Введите название города");
                return sendMessage;
            }
        } else {

            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            String chatId = String.valueOf(message.getChatId());
            sendMessage.setChatId(chatId);
            User user = userService.getOrCreateUser(chatId);

            if (message.hasText()) {
                if (message.getText().toLowerCase().equals("weather") || message.getText().toLowerCase().equals("погода")) {
                    String city = user.getCity();
                    WeatherModel weather = weatherService.getWeatherInCity(city);
                    sendMessage.setText("Температура - " + weather.getTemp() + "\n" +
                            "Ощущается как - " + weather.getFeels_like() + "\n" +
                            "Давление - " + weather.getPressure() + "\n");
                    return sendMessage;
                }
                if (message.getText().equals("/weather")) {

                    InlineKeyboardMarkup keyBoard = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
                    List<InlineKeyboardButton> row = new ArrayList<>();
                    InlineKeyboardButton buttonWeather = new InlineKeyboardButton();
                    buttonWeather.setText(String.valueOf("Узнать погоду"));
                    buttonWeather.setCallbackData(String.valueOf(Queries.WEATHER));
                    InlineKeyboardButton buttonSetCity = new InlineKeyboardButton();
                    buttonSetCity.setText(String.valueOf("Изменить город"));
                    buttonSetCity.setCallbackData(String.valueOf(Queries.SET_CITY));

                    row.add(buttonWeather);
                    row.add(buttonSetCity);
                    rows.add(row);
                    keyBoard.setKeyboard(rows);


                    sendMessage.setReplyMarkup(keyBoard);
                    sendMessage.setText("-- Узнать погоду --");

                    return sendMessage;
                }
                if (state.equals(States.CITY_WAITING)) {
                    if (weatherService.checkCity(message.getText().toLowerCase())) {
                        userService.saveUser(chatId,message.getText().toLowerCase());
                        sendMessage.setText("Город установлен!");
                    } else {
                        sendMessage.setText("Такой город не обнаружен(");
                    }
                    state = States.DEFAULT;
                    return sendMessage;
                }
                sendMessage.setText("Напишите /weather лио идите нау\n");
                return sendMessage;
            }

        }
        return null;

    }
}
