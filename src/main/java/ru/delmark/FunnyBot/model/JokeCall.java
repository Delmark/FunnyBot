package ru.delmark.FunnyBot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
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

    public JokeCall(Long id, Joke joke, Date callDate) {
        this.id = id;
        this.joke = joke;
        this.callDate = callDate;
    }

    @Id
    @GeneratedValue(generator = "jokeCall_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "jokeCall_seq", name = "jokeCall_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Joke_id")
    private Joke joke;

    @Column(name = "user_id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private Long userId;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "call_date")
    private Date callDate;
}
