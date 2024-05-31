package ru.delmark.FunnyBot.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.exceptions.NoSuchJokeException;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.model.JokeCall;
import ru.delmark.FunnyBot.repository.JokeRepository;
import ru.delmark.FunnyBot.utils.NowService;

import java.util.*;

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

        return jokeRepository.save(joke);
    }

    @Transactional
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
        Joke jokeForEdit = jokeRepository.findById(id).orElseThrow(NoSuchJokeException::new);

        jokeForEdit.setJoke(joke.getJoke());
        jokeForEdit.setUpdateDate(nowService.getCurrentDate());
        jokeRepository.save(jokeForEdit);
        return Optional.of(jokeForEdit);
    }

    @Override
    public void deleteJoke(Long id) {
        Joke joke = jokeRepository.findById(id).orElseThrow(NoSuchJokeException::new);

        jokeRepository.delete(joke);
    }

    @Override
    public Page<Joke> getAllJokes(int page) {
        return jokeRepository.findAll(PageRequest.of(page, 10));
    }

    @Transactional
    @Override
    public Joke getRandomJoke(Long userID) {
        List<Joke> allJokes = jokeRepository.findAll();
        Joke randomJoke = allJokes.get(new Random().nextInt(0, allJokes.size()));
        randomJoke.getJokeCalls().add(new JokeCall(null, randomJoke,userID , nowService.getCurrentDate()));
        return jokeRepository.saveAndFlush(randomJoke);
    }

    @Transactional
    @Override
    public List<Joke> getTop5Jokes() {
        return jokeRepository.getTopJokes();
    }

    @Override
    public Joke getRandomJoke() {
        Joke joke = jokeRepository.getRandomJoke();
        joke.getJokeCalls().add(new JokeCall(null, joke, nowService.getCurrentDate()));
        jokeRepository.saveAndFlush(joke);
        return joke;
    }
}
