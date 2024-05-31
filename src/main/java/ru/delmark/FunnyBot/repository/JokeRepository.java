package ru.delmark.FunnyBot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.model.JokeCall;

import java.util.List;
import java.util.Optional;

public interface JokeRepository extends PagingAndSortingRepository<Joke, Long>, JpaRepository<Joke, Long> {
    void delete(Joke joke);

    @Query("select new Joke(j.id, j.joke, j.creationDate, j.updateDate) from Joke j right join JokeCall jc on jc.joke.id = j.id group by j.id, j.joke, j.creationDate, j.updateDate order by count(*) DESC LIMIT 5")
    List<Joke> getTopJokes();

    @Query(value = "SELECT j FROM Joke j ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Joke getRandomJoke();
}