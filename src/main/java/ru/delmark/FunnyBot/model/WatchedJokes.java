package ru.delmark.FunnyBot.model;

import com.pengrad.telegrambot.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.collection.spi.PersistentBag;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity(name="WatchedJokes")
@Table(name="WatchedJokes")
public class WatchedJokes {
    @Id
    @Column(name = "UserId")
    private long userId;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private PersistentBag<Joke> watchedJokesList;
}
