package ru.delmark.FunnyBot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "Joke_Call")
public class JokeCall {

    @Id
    @GeneratedValue(generator = "jokeCall_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "jokeCall_seq", name = "jokeCall_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Joke_id")
    private Joke joke;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "call_date")
    private Date callDate;
}
