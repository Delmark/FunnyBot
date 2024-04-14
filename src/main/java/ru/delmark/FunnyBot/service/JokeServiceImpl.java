package ru.delmark.FunnyBot.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.repository.JokeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class JokeServiceImpl implements JokeService{

    private final JokeRepository jokeRepository;

    @Override
    public Joke addJoke(Joke joke) {
        joke.setUpdateDate(LocalDate.now());
        joke.setCreationDate(LocalDate.now());
        return jokeRepository.save(joke);
    }

    @Override
    public Optional<Joke> getJokebyId(Long id) {
        return jokeRepository.findById(id);
    }

    @Override
    public Optional<Joke> editJoke(Long id, Joke joke) {
        Optional<Joke> jokeForEdit = jokeRepository.findById(id);

        if (jokeForEdit.isPresent()) {
            Joke editedJoke = jokeForEdit.get();
            editedJoke.setJoke(joke.getJoke());
            editedJoke.setUpdateDate(LocalDate.now());
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
            jokeRepository.delete(joke.get());
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
