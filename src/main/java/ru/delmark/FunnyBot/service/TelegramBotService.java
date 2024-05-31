package ru.delmark.FunnyBot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelegramBotService {

    private final HashMap<User, Integer> userPage = new HashMap<>();

    private final JokeService jokeService;
    private final TelegramBot bot;

    private static final Keyboard keyboard = new ReplyKeyboardMarkup(
            new KeyboardButton("Хочу шутку"),
            new KeyboardButton("Хочу все шутки"),
            new KeyboardButton("Вывести топ 5 шуток")
    );

    private static final InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
            new InlineKeyboardButton("Следующая страница").callbackData("nextPage")
    );

    @Autowired
    public TelegramBotService(TelegramBot telegramBot, JokeService jokeService) {
        this.bot = telegramBot;
        this.jokeService = jokeService;

        bot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                handleUpdate(update);
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    public void handleUpdate(Update update) {
        if (update.callbackQuery() == null) {
            String command = handleKeyboardInput(update.message());

            switch (command) {
                case "/start" -> onStartCommand(update);
                case "/joke" -> onJokeCommand(update);
                case "/getAllJokes" -> onAllJokesCommand(update);
                case "/getTop5Jokes" -> onTopJokesCommand(update);
                default -> onUnknownCommand(update);
            }
        }
        else {
            CallbackQuery callbackQuery = update.callbackQuery();
            onAllJokesPaginationCallback(callbackQuery);
        }
    }

    public void onStartCommand(Update update) {
        String response = "Чат-бот с анекдотами!\nЗдесь хранится набор самых смешных и не очень анекдотов.\n\nЕсли хотите услышать <strong>случайный анекдот</strong> введите /joke или нажмите на кнопку!";
        bot.execute(
                new SendMessage(update.message().chat().id(), response)
                        .parseMode(ParseMode.HTML)
                        .disableNotification(true)
                        .replyMarkup(keyboard)
        );
    }

    public void onJokeCommand(Update update) {
        String response = jokeService.getRandomJoke(update.message().from().id()).getJoke();
        bot.execute(
                new SendMessage(update.message().chat().id(), response)
                        .parseMode(ParseMode.HTML)
                        .disableNotification(true)
                        .replyMarkup(keyboard)
        );
    }

    public void onTopJokesCommand(Update update) {
        List<String> jokes = jokeService.getTop5Jokes().stream().map(Joke::getJoke).collect(Collectors.toList());
        String response = String.join("\n\n", jokes);
        bot.execute(
                new SendMessage(update.message().chat().id(), response)
                        .parseMode(ParseMode.HTML)
                        .disableNotification(true)
                        .replyMarkup(keyboard)
        );
    }

    public void onAllJokesCommand(Update update) {
        String response;
        Page<Joke> page = jokeService.getAllJokes(0);

        if (userPage.containsKey(update.message().from())) {
            int currentPage = userPage.get(update.message().from());
            if (currentPage + 1 > page.getTotalPages()) {
                response = handlePage(page);
            }
            else {
                userPage.put(update.message().from(), currentPage + 1);
                response = handlePage(jokeService.getAllJokes(currentPage + 1));
            }
        } else {
            userPage.put(update.message().from(), 0);
            response = handlePage(page);
        }

        bot.execute(new SendMessage(update.message().chat().id(), response)
                .parseMode(ParseMode.HTML)
                .disableNotification(true)
                .replyMarkup(inlineKeyboard));
    }

    public void onUnknownCommand(Update update) {
        bot.execute(new SendMessage(update.message().chat().id(), "Неизвестная команда!")
                .parseMode(ParseMode.HTML)
                .disableNotification(true)
                .replyMarkup(keyboard));
    }

    public void onAllJokesPaginationCallback(CallbackQuery callbackQuery) {

        String answer;

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

    private String handleKeyboardInput(Message message) {
        String text = message.text();

        switch (text) {
            case "Хочу шутку" -> {
                return "/joke";
            }
            case "Хочу все шутки" -> {
                return "/getAllJokes";
            }
            case "Вывести топ 5 шуток" -> {
                return "/getTop5Jokes";
            }
        }

        return text;
    }
}
