package ru.delmark.FunnyBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.model.JokeCall;

import java.util.List;
import java.util.Optional;

public interface JokeRepository extends PagingAndSortingRepository<Joke, Long>, JpaRepository<Joke, Long> {
    void delete(Joke joke);

}