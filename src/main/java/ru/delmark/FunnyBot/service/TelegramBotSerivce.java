package ru.delmark.FunnyBot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
public class TelegramBotSerivce {

    private final HashMap<User, Integer> userPage = new HashMap<>();

    private final JokeService jokeService;
    private final TelegramBot bot;

    private static final Keyboard keyboard = new ReplyKeyboardMarkup(
            new KeyboardButton("Хочу шутку"),
            new KeyboardButton("Хочу все шутки")
    );

    private static final InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
            new InlineKeyboardButton("Следующая страница").callbackData("nextPage")
    );

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

        boolean forLine = false;
        String answer;


        if (update.callbackQuery() == null) {
            String message = update.message().text();

            if (message.startsWith("/start")) {
                answer = "Чат-бот с анекдотами!\nЗдесь хранится набор самых смешных и не очень анекдотов.\n\nЕсли хотите услышать <strong>случайный анекдот</strong> введите /joke или нажмите на кнопку!";
            } else if (message.startsWith("/joke") || message.equals("Хочу шутку")) {
                answer = jokeService.getRandomJoke().getJoke();
            } else if (message.startsWith("/getAllJokes") || message.equals("Хочу все шутки")) {
                forLine = true;
                Page<Joke> page = jokeService.getAllJokes(0);
                if (userPage.containsKey(update.message().from())) {
                    int currentPage = userPage.get(update.message().from());
                    if (currentPage + 1 > page.getTotalPages()) {
                        answer = handlePage(page);
                    }
                    else {
                        userPage.put(update.message().from(), currentPage + 1);
                        answer = handlePage(jokeService.getAllJokes(currentPage + 1));
                    }
                } else {
                    userPage.put(update.message().from(), 0);
                    answer = handlePage(page);
                }
            } else {
                answer = "Неизвестная команда!";
            }

            BaseRequest request;

            if (!forLine) {
                request = new SendMessage(update.message().chat().id(), answer)
                        .parseMode(ParseMode.HTML)
                        .disableNotification(true)
                        .replyMarkup(keyboard);
            }
            else {
                request = new SendMessage(update.message().chat().id(), answer)
                        .parseMode(ParseMode.HTML)
                        .disableNotification(true)
                        .replyMarkup(inlineKeyboard);
            }

            bot.execute(request);
        }
        else {
            CallbackQuery callbackQuery = update.callbackQuery();

            if (callbackQuery.data().equals("nextPage")) {
                Page<Joke> page = jokeService.getAllJokes(0);
                User author = callbackQuery.from();

                if (userPage.containsKey(author)) {
                        int currentPage = userPage.get(callbackQuery.from());
                        if (currentPage + 1 > page.getTotalPages()) {
                            userPage.put(callbackQuery.from(), 0);
                            answer = handlePage(page);
                        }
                        else {
                            userPage.put(callbackQuery.from(), currentPage + 1);
                            answer = handlePage(jokeService.getAllJokes(currentPage + 1));
                        }
                }
                else {
                    userPage.put(callbackQuery.from(), 0);
                    answer = handlePage(page);
                }

                bot.execute(new EditMessageText(callbackQuery.message().chat().id(), callbackQuery.message().messageId(), answer).replyMarkup(inlineKeyboard));
            }
        }
    }

    private String handlePage(Page<Joke> page) {
        StringBuilder data = new StringBuilder();
        List<Joke> content = page.getContent();
        if (!content.isEmpty()) {
            data.append("Страница ").append(page.getNumber()).append(" из ").append(page.getTotalPages()).append("\n\n");
            for (Joke joke : content) {
                data.append(joke.getJoke()).append("\n\n\n");
            }
        }
        else {
            data.append("Конец анекдотов!");
        }

        return data.toString();
    }
}
