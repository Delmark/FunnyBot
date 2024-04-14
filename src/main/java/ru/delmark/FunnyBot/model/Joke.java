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
@ToString
@Entity(name="Joke")
@Table(name="Joke")
public class Joke {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "joke", cascade = CascadeType.ALL)
    private List<JokeCall> jokeCalls;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Joke joke = (Joke) o;
        return getId() != null && Objects.equals(getId(), joke.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
