package ru.delmark.FunnyBot.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.delmark.FunnyBot.model.Joke;
import ru.delmark.FunnyBot.service.JokeService;

import java.util.List;
import java.util.Optional;


// GET /jokes - выдача всех шуток
// GET /jokes/id - выдача шутки с определенным ID
// POST /jokes - создание новой шутки
// PUT /jokes/id - изменение шутки
// DELETE /jokes/id - удаление шутки

@RestController
@AllArgsConstructor
@RequestMapping("/jokes")
public class JokeController {

    private final JokeService jokeService;

    @PostMapping
    public ResponseEntity<Joke> createJoke(@RequestBody Joke joke) {
        return ResponseEntity.ok(jokeService.addJoke(joke));
    }

    @GetMapping
    public ResponseEntity<Page<Joke>> getAllJokes(@RequestParam Integer page) {
        return ResponseEntity.ok(jokeService.getAllJokes(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Joke> getJokeById(@PathVariable(name = "id") Long id) {
        Optional<Joke> joke = jokeService.getJokebyId(id);
        return joke.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }


    @GetMapping("/topJokes")
    private ResponseEntity<List<Joke>> getTop5Jokes() {
        return ResponseEntity.ok(jokeService.getTop5Jokes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Joke> editJoke(@PathVariable(name = "id") Long id, @RequestBody Joke jokeForEdit) {
        Optional<Joke> editedJoke = jokeService.editJoke(id, jokeForEdit);

        return editedJoke.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJoke(@PathVariable(name = "id") Long id) {
        jokeService.deleteJoke(id);
        return ResponseEntity.ok().build();
    }
}
