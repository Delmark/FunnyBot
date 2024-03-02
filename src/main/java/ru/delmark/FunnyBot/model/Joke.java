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
    long id;

    @Column(name="Joke")
    String joke;

    @Column(name = "creation_date")
    LocalDate creationDate;

    @Column(name = "update_date")
    LocalDate updateDate;
}
