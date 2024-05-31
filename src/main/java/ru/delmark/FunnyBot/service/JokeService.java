package ru.delmark.FunnyBot.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;

import java.util.List;
import java.util.Optional;

@Service
public interface JokeService {
    Joke addJoke(Joke joke);
    Optional<Joke> getJokebyId(Long id);
    Optional<Joke> editJoke(Long id, Joke joke);
    void deleteJoke(Long id);
    Page<Joke> getAllJokes(int page);
    Joke getRandomJoke(Long userId);
    List<Joke> getTop5Jokes();
    Joke getRandomJoke();
}
