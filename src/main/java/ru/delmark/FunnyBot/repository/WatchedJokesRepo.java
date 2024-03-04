package ru.delmark.FunnyBot.repository;


import com.pengrad.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.model.WatchedJokes;

import java.util.List;
import java.util.Optional;

public interface WatchedJokesRepo extends JpaRepository<WatchedJokes, Long> {
}
