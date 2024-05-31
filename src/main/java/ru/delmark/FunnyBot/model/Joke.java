package ru.delmark.FunnyBot.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Entity(name="Joke")
@Table(name="Joke")
public class Joke {

    public Joke(Long id, String joke, Date creationDate, Date updateDate) {
        this.id = id;
        this.joke = joke;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "joke_seq",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "joke_seq", name = "joke_seq", allocationSize = 1)
    private Long id;

    @Lob
    @Column(name="Joke", columnDefinition = "TEXT")
    private String joke;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "creation_date")
    private Date creationDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "update_date")
    private Date updateDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ToString.Exclude
    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL)
    private List<JokeCall> jokeCalls;

    private int size() {
        return jokeCalls.size();
    }

}
