package ru.delmark.FunnyBot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.model.JokeCall;
import ru.delmark.FunnyBot.repository.JokeRepository;
import ru.delmark.FunnyBot.utils.NowService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class JokeServiceImpl implements JokeService{

    private final JokeRepository jokeRepository;
    private final NowService nowService;

    @Override
    public Joke addJoke(Joke joke) {
        Date currentDate = nowService.getCurrentDate();

        joke.setUpdateDate(currentDate);
        joke.setCreationDate(currentDate);
        joke.setJokeCalls(new ArrayList<>());

        return jokeRepository.save(joke);
    }

    @Override
    public Optional<Joke> getJokebyId(Long id) {
        Optional<Joke> joke = jokeRepository.findById(id);
        joke.ifPresent(value -> {
            value.getJokeCalls().add(new JokeCall(null, value, nowService.getCurrentDate()));
            jokeRepository.saveAndFlush(value);
        });
        return jokeRepository.findById(id);
    }

    @Override
    public Optional<Joke> editJoke(Long id, Joke joke) {
        Optional<Joke> jokeForEdit = jokeRepository.findById(id);

        if (jokeForEdit.isPresent()) {
            Joke editedJoke = jokeForEdit.get();
            editedJoke.setJoke(joke.getJoke());
            editedJoke.setUpdateDate(nowService.getCurrentDate());
            jokeRepository.save(editedJoke);
            return Optional.of(editedJoke);
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteJoke(Long id) {
        Optional<Joke> joke = jokeRepository.findById(id);

        if (joke.isPresent()) {
            jokeRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<Joke> getAllJokes() {
        return jokeRepository.findAll();
    }
}
