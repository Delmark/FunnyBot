package ru.delmark.FunnyBot.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;

import java.util.List;
import java.util.Optional;

@Service
public interface JokeService {
    public Joke addJoke(Joke joke);
    public Optional<Joke> getJokebyId(Long id);
    public Optional<Joke> editJoke(Long id, Joke joke);
    public boolean deleteJoke(Long id);
    public Page<Joke> getAllJokes(int page);
    public Joke getRandomJoke(Long userId);
    public List<Joke> getTop5Jokes();
}
