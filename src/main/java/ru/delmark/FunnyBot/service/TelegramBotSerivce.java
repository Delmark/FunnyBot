package ru.delmark.FunnyBot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;

import java.util.List;
import java.util.Random;

@Service
public class TelegramBotSerivce {

    private final JokeService jokeService;
    private final TelegramBot bot;

    private static final Keyboard keyboard = new ReplyKeyboardMarkup(new KeyboardButton("Хочу шутку"));

    @Autowired
    public TelegramBotSerivce(TelegramBot telegramBot, JokeService jokeService) {
        this.bot = telegramBot;
        this.jokeService = jokeService;

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleUpdate(Update update) {
        String answer;

        String message = update.message().text();

        if (message.startsWith("/start")) {
            answer = "Чат-бот с анекдотами!\nЗдесь хранится набор самых смешных и не очень анекдотов.\n\nЕсли хотите услышать <strong>случайный анекдот</strong> введите /joke или нажмите на кнопку!";
        } else if (message.startsWith("/joke") || message.equals("Хочу шутку")) {
            answer = jokeService.getRandomJoke().getJoke();
        }
        else {
            answer = "Неизвестная команда!";
        }

        SendMessage request = new SendMessage(update.message().chat().id(), answer)
                .parseMode(ParseMode.HTML)
                .disableNotification(true)
                .replyMarkup(keyboard);
        bot.execute(request);
    }


}
