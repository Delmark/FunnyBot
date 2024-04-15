package ru.delmark.FunnyBot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.model.JokeCall;
import ru.delmark.FunnyBot.repository.JokeRepository;
import ru.delmark.FunnyBot.utils.NowService;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JokeControllerTest {

    private final Date date = new Date();

    NowService nowService = Mockito.mock(NowService.class);
    @BeforeEach
    void setUp() {
        Mockito.when(nowService.getCurrentDate()).thenReturn(date);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JokeRepository jokeRepository;

    private static ObjectMapper objectMapper = new ObjectMapper().setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

    @Test
    @DisplayName("Создание шутки")
    void createJoke() throws Exception {
        Joke inputJoke = new Joke(null, "Test joke", null, null, null);
        Joke expectedJoke = new Joke(1L, "Test joke", date, date);

        mockMvc.perform(MockMvcRequestBuilders.post("/jokes").
                content(objectMapper.writeValueAsString(inputJoke)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedJoke)));

        // Lazy Exception??
        assertEquals(expectedJoke, jokeRepository.findById(1L).get());
    }

    @Test
    @DisplayName("Получение всех шуток")
    void getAllJokes() throws Exception {
        Joke savedJoke = new Joke(1L, "Test Joke", date, date, new ArrayList<>());
        jokeRepository.saveAndFlush(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.get("/jokes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(List.of((savedJoke)))));
    }

    @Test
    @DisplayName("Получение шутки по ID")
    void getJokeById() throws Exception {
        Joke savedJoke = new Joke(1L, "TestJoke", date, date, new ArrayList<>());
        Joke expectedJoke = new Joke(1L, "TestJoke", date, date, List.of(new JokeCall(1L, savedJoke, date)));
        jokeRepository.saveAndFlush(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.get("/jokes/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedJoke)));
    }

    @Test
    @DisplayName("Редактирование шутки")
    void editJoke() throws Exception {
        Joke savedJoke = new Joke(1L, "The man put on a hat,", date, date, new ArrayList<>());
        Joke inputJoke = new Joke(null, "The man put on a hat, and it was just for him", null, null, null);
        Joke expectedJoke = new Joke(1L, "The man put on a hat, and it was just for him", date, date, new ArrayList<>());
        jokeRepository.save(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.put("/jokes/1")
                .content(objectMapper.writeValueAsString(inputJoke)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(expectedJoke)));

        // Lazy Exception??
        assertEquals(expectedJoke, jokeRepository.findById(1L).get());
    }

    @Test
    @DisplayName("Удаление шутки")
    void deleteJoke() throws Exception {
        Joke savedJoke = new Joke(1L, "The man put on a hat, and it was just for him", date, date, new ArrayList<>());
        jokeRepository.save(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.delete("/jokes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(jokeRepository.findById(1L), Optional.empty());
    }
}