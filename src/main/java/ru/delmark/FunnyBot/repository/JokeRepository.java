package ru.delmark.FunnyBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.delmark.FunnyBot.model.Joke;

import java.util.Optional;

public interface JokeRepository extends JpaRepository<Joke, Long> {
    void delete(Joke joke);
}
