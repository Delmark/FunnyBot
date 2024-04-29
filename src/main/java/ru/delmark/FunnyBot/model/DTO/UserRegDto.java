package ru.delmark.FunnyBot.model.DTO;

import lombok.Value;
import ru.delmark.FunnyBot.model.User;

import java.io.Serializable;

/**
 * DTO for {@link User}
 */
@Value
public class UserRegDto implements Serializable {
    String username;
    String password;
}