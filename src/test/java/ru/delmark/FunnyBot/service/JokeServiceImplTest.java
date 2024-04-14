package ru.delmark.FunnyBot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.repository.JokeRepository;
import ru.delmark.FunnyBot.utils.NowService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JokeServiceImplTest {

    private final Date date = new Date();

    private static NowService nowService = Mockito.mock(NowService.class);

    @BeforeEach
    void setUp() {
        Mockito.when(nowService.getCurrentDate()).thenReturn(date);
    }

    private static JokeRepository jokeRepository = Mockito.mock(JokeRepository.class);

    private static JokeService jokeService = new JokeServiceImpl(jokeRepository, nowService);

    @DisplayName("Тест на добавление шутки")
    @Test
    void addJoke() {
        Joke inputJoke = new Joke(null, "Надел грузин шляпу, а она ему как раз", null, null);
        Joke expectedJoke = new Joke(1L, "Надел грузин шляпу, а она ему как раз", date, date, new ArrayList<>());
        Joke expectedToSave = new Joke(inputJoke.getId(), inputJoke.getJoke(), date, date, new ArrayList<>());
        Mockito.when(jokeRepository.save(expectedToSave)).
                thenReturn(expectedJoke);
        assertEquals(jokeService.addJoke(inputJoke), expectedJoke);
    }

    @DisplayName("Тест на редактирование шутки")
    @Test
    void editJoke() {
        Joke inputJoke = new Joke(1L, "Надел грузин шляпу, а она ему как раз", null, null, null);
        Joke expected = new Joke(1L, "Надел грузин шляпу, а она ему как раз", date, date, new ArrayList<>());
        Joke returnedJoke = new Joke(1L, "Надел грузин шляпу,", date, date, new ArrayList<>());
        Mockito.when(jokeRepository.findById(1L)).thenReturn(Optional.of(returnedJoke));
        Mockito.when(jokeRepository.save(inputJoke)).thenReturn(expected);
        assertEquals(expected, jokeService.editJoke(1L, inputJoke).get());
    }


    @DisplayName("Тест на редактирование несуществующей шутки")
    @Test
    void editNonExistingJoke() {
        Joke inputJoke = new Joke(1L, "Надел грузин шляпу, а она ему как раз", null, null, null);
        Mockito.when(jokeRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), jokeService.editJoke(1L, inputJoke));
    }

    @DisplayName("Тест на удаление несуществующей шутки")
    @Test
    void deleteNonExistingJoke() {
        jokeService.deleteJoke(1L);
        Mockito.verify(jokeRepository, Mockito.times(1)).findById(1L);
        assertEquals(false, jokeService.deleteJoke(1L));
    }

    @DisplayName("Тест на удаление шутки")
    @Test
    void deleteJoke() {
        jokeService.deleteJoke(1L);
        Mockito.when(jokeRepository.findById(1L)).thenReturn(Optional.of(new Joke(1L, "Joke for Delete", null, null, null)));
        Mockito.verify(jokeRepository, Mockito.times(1)).deleteById(1L);
        assertTrue(jokeService.deleteJoke(1L));
    }
}