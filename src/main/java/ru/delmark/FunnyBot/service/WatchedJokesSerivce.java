package ru.delmark.FunnyBot.service;

import com.pengrad.telegrambot.model.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.spi.PersistentBag;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.model.WatchedJokes;
import ru.delmark.FunnyBot.repository.WatchedJokesRepo;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@AllArgsConstructor
@Service
public class WatchedJokesSerivce {

    private final WatchedJokesRepo watchedJokesRepo;

    public void addUser(User user) {
        WatchedJokes watchedJokes = new WatchedJokes();
        watchedJokes.setUserId(user.id());
        watchedJokes.setWatchedJokesList(new PersistentBag<>());
        watchedJokesRepo.save(watchedJokes);
    }


    @Transactional
    public void resetWatchedJokes(User user) {
        Optional<WatchedJokes> watchedJokesModel = watchedJokesRepo.findById(user.id());
        if (watchedJokesModel.isPresent()) {
            WatchedJokes watchedJokes = watchedJokesModel.get();
            watchedJokes.getWatchedJokesList().clear();
        }
    }

    public boolean userExists(User user) {
        return watchedJokesRepo.existsById(user.id());
    }

    public boolean isWatchedJoke(User user, Joke joke) {
        Optional<WatchedJokes> watchedJokesModel = watchedJokesRepo.findById(user.id());

        if (watchedJokesModel.isPresent()) {
            WatchedJokes watchedJokes = watchedJokesModel.get();
            PersistentBag<Joke> watchedJokesList = watchedJokes.getWatchedJokesList();

            if (!watchedJokesList.isEmpty()) {
                for (Joke jokeFromList : watchedJokesList) {
                    if (jokeFromList.getId() == joke.getId()) {
                        return true;
                    }
                }
            }
            return false;
        }
        else {
            return false;
        }
    }


    @Transactional
    public void addJokeToWatched(User user, Joke joke) {
        Optional<WatchedJokes> watchedJokesModel = watchedJokesRepo.findById(user.id());

        if (watchedJokesModel.isPresent()) {
            WatchedJokes watchedJokes = watchedJokesModel.get();
            PersistentBag<Joke> watchedJokesList = watchedJokes.getWatchedJokesList();
            watchedJokesList.add(joke);
        }
    }
}
