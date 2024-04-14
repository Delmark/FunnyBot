package ru.delmark.FunnyBot.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.repository.JokeRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JokeServiceImplTest {

    private static JokeRepository jokeRepository = Mockito.mock(JokeRepository.class);

    private static JokeService jokeService = new JokeServiceImpl(jokeRepository);

    @DisplayName("Тест на добавление шутки")
    @Test
    void addJoke() {
        Joke inputJoke = new Joke(null, "Надел грузин шляпу, а она ему как раз", null, null);
        Joke expectedJoke = new Joke(1L, "Надел грузин шляпу, а она ему как раз", LocalDate.now(), LocalDate.now());
        jokeService.addJoke(inputJoke);
        Joke expectedToSave = new Joke(null, "Надел грузин шляпу, а она ему как раз", LocalDate.now(), LocalDate.now());
        Mockito.when(jokeRepository.save(expectedToSave)).
                thenReturn(expectedJoke);
        assertEquals(expectedJoke, jokeService.addJoke(inputJoke));
    }

    @DisplayName("Тест на редактирование шутки")
    @Test
    void editJoke() {
        Joke inputJoke = new Joke(1L, "Надел грузин шляпу, а она ему как раз", null, null);
        Joke expected = new Joke(1L, "Надел грузин шляпу, а она ему как раз", LocalDate.now().minusDays(1), LocalDate.now());
        jokeService.editJoke(1L, inputJoke);
        Joke returnedJoke = new Joke(1L, "Надел грузин шляпу,", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1));
        Mockito.when(jokeRepository.findById(1L)).thenReturn(Optional.of(returnedJoke));
        Mockito.when(jokeRepository.save(inputJoke)).thenReturn(expected);
        assertEquals(expected, jokeService.editJoke(1L, inputJoke).get());
    }


    @DisplayName("Тест на редактирование несуществующей шутки")
    @Test
    void editNonExistingJoke() {
        Joke inputJoke = new Joke(1L, "Надел грузин шляпу, а она ему как раз", null, null);
        jokeService.editJoke(1L, inputJoke);
        Mockito.when(jokeRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(false, jokeService.editJoke(1L, inputJoke).isPresent());
    }

}