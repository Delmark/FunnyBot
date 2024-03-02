package ru.delmark.FunnyBot.service;

import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public interface JokeService {
    public Joke addJoke(Joke joke);
    public Optional<Joke> getJokebyId(long id);
    public boolean editJoke(long id, Joke joke);
    public boolean deleteJoke(long id);
    public List<Joke> getAllJokes();
}
