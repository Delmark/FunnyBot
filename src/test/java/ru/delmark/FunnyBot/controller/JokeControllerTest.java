package ru.delmark.FunnyBot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.repository.JokeRepository;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JokeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JokeRepository jokeRepository;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // Костыль для сериализации шутки, оказывается что
    // ObjectWrapper нормально НЕ поддерживает сериализацию LocalDate,
    // даже при подключении JavaTimeModule, поэтому своя сделана собственная реализация.
    private static String JokeWrapper(Joke joke) {
        return String.format("{\"id\":%d,\"joke\":\"%s\",\"creationDate\":\"%s\",\"updateDate\":\"%s\"}",
                joke.getId(), joke.getJoke(), joke.getCreationDate(), joke.getUpdateDate());
    }

    @Test
    @DisplayName("Создание шутки")
    void createJoke() throws Exception {
        Joke inputJoke = new Joke(null, "Test joke", null, null);
        Joke expectedJoke = new Joke(1L, "Test joke", LocalDate.now(), LocalDate.now());

        mockMvc.perform(MockMvcRequestBuilders.post("/jokes").
                content(objectMapper.writeValueAsString(inputJoke)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(JokeWrapper(expectedJoke)));
        assertEquals(jokeRepository.findById(1L).get(), expectedJoke);
    }

    @Test
    void getAllJokes() throws Exception {
        Joke savedJoke = new Joke(1L, "Test Joke", LocalDate.now(), LocalDate.now());
        jokeRepository.save(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.get("/jokes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(List.of(JokeWrapper(savedJoke)).toString()));
    }

    @Test
    void getJokeById() throws Exception {
        Joke savedJoke = new Joke(1L, "TestJoke", LocalDate.now(), LocalDate.now());
        jokeRepository.save(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.get("/jokes/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(JokeWrapper(savedJoke)));
    }

    @Test
    void editJoke() throws Exception {
        Joke savedJoke = new Joke(1L, "The man put on a hat,", LocalDate.now(), LocalDate.now());
        Joke inputJoke = new Joke(null, "The man put on a hat, and it was just for him", null, null);
        Joke expectedJoke = new Joke(1L, "The man put on a hat, and it was just for him", LocalDate.now(), LocalDate.now());
        jokeRepository.save(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.put("/jokes/1")
                .content(objectMapper.writeValueAsString(inputJoke)).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(JokeWrapper(expectedJoke)));
        assertEquals(jokeRepository.findById(1L).get(), expectedJoke);
    }

    @Test
    void deleteJoke() throws Exception {
        Joke savedJoke = new Joke(1L, "The man put on a hat, and it was just for him", LocalDate.now(), LocalDate.now());
        jokeRepository.save(savedJoke);
        mockMvc.perform(MockMvcRequestBuilders.delete("/jokes/1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertEquals(jokeRepository.findById(1L), Optional.empty());
    }
}