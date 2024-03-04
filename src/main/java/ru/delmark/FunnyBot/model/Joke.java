package ru.delmark.FunnyBot.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity(name="Joke")
@Table(name="Joke")
public class Joke {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Lob
    @Column(name="Joke", columnDefinition = "TEXT")
    private String joke;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Column(name = "update_date")
    private LocalDate updateDate;

    @ManyToOne
    private WatchedJokes user;
}
