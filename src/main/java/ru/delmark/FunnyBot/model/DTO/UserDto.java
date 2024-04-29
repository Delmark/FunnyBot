package ru.delmark.FunnyBot.model.DTO;

import lombok.Value;
import ru.delmark.FunnyBot.model.Role;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO for {@link ru.delmark.FunnyBot.model.User}
 */
@Value
public class UserDto implements Serializable {
    Long id;
    String username;
    boolean enabled;
    Set<Role> roles;
}