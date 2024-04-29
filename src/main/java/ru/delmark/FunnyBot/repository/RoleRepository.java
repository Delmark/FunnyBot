package ru.delmark.FunnyBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.delmark.FunnyBot.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByAuthority(String role);
}