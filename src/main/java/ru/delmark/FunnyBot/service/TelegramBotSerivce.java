package ru.delmark.FunnyBot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.repository.WatchedJokesRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class TelegramBotSerivce {

    private final JokeService jokeService;
    private final TelegramBot bot;
    private final WatchedJokesSerivce watchedJokesSerivce;

    private static final Keyboard keyboard = new ReplyKeyboardMarkup(new KeyboardButton("Хочу шутку"), new KeyboardButton("Хочу шутку которую ещё не видел!"));

    @Autowired
    public TelegramBotSerivce(TelegramBot telegramBot, JokeService jokeService, WatchedJokesSerivce watchedJokesSerivce) {
        this.bot = telegramBot;
        this.jokeService = jokeService;
        this.watchedJokesSerivce = watchedJokesSerivce;

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.inlineQuery() == null) {
                    handleUpdate(update);
                }
                else {
                    handleInlineQuery(update.inlineQuery());
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void handleInlineQuery(InlineQuery query) {
        List<Joke> jokesList = jokeService.getAllJokes();
        Random rng = new Random();
        int randomNum = rng.nextInt(jokesList.size());
        InlineQueryResultArticle answer = new InlineQueryResultArticle("1",
                "Случайный анекдот",
                new InputTextMessageContent(jokesList.get(randomNum).getJoke()));

        bot.execute(new AnswerInlineQuery(query.id(), answer).cacheTime(0));
    }

    private void handleUpdate(Update update) {
        String answer = null;

        String message = update.message().text();

        List<Joke> jokesList = jokeService.getAllJokes();

        if (message.startsWith("/start")) {
            answer = "Чат-бот с анекдотами!\nЗдесь хранится набор самых смешных и не очень анекдотов.\n\nЕсли хотите услышать <strong>случайный анекдот</strong> введите /joke или нажмите на кнопку!";
        } else if (message.startsWith("/joke") || message.equals("Хочу шутку")) {
            Random rng = new Random();
            answer = jokesList.get(rng.nextInt(jokesList.size())).getJoke();
        } else if (message.equalsIgnoreCase("/getUniqueJoke") || message.equals("Хочу шутку которую ещё не видел!")) {
            User messageSender = update.message().from();

            if (!watchedJokesSerivce.userExists(messageSender)) {
                watchedJokesSerivce.addUser(messageSender);
            }

            boolean foundJoke = false;
            for (Joke joke : jokesList) {
                if (!watchedJokesSerivce.isWatchedJoke(messageSender, joke)) {
                    answer = joke.getJoke();
                    foundJoke = true;
                    watchedJokesSerivce.addJokeToWatched(messageSender, joke);
                    break;
                }
            }
            if (!foundJoke) {
                answer = "У нас кончились шутки :(";
            }
        } else if (message.equals("/resetJokes")) {
            User messageSender = update.message().from();

            if (watchedJokesSerivce.userExists(messageSender)) {
                watchedJokesSerivce.resetWatchedJokes(messageSender);
                answer = "Просмотренные шутки сброшенны!";
            } else {
                answer = "Вы не просмотрели ни одной шутки!";
            }
        } else {
            answer = "Неизвестная команда!";
        }

        SendMessage request = new SendMessage(update.message().chat().id(), answer)
                .parseMode(ParseMode.HTML)
                .disableNotification(true)
                .replyMarkup(keyboard);
        bot.execute(request);
    }


}
