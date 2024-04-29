package ru.delmark.FunnyBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.delmark.FunnyBot.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);

}
