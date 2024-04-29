package ru.delmark.FunnyBot.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Component
public class ExceptionsHandlers {
    @ExceptionHandler(NoSuchRoleException.class)
    public ResponseEntity<ErrorResponse> handleException(NoSuchRoleException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        400L,
                        "Такой роли не существует"
                )
        );
    }

    @ExceptionHandler(NoSuchUserException.class)
    public ResponseEntity<ErrorResponse> handleException(NoSuchUserException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        400L,
                        "Пользователя с таким индетификатором не существует"
                )
        );
    }

    @ExceptionHandler(UserDoesNotHaveRoleException.class)
    public ResponseEntity<ErrorResponse> handleException(UserDoesNotHaveRoleException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        400L,
                        "У пользователя нет такой роли"
                )
        );
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(UsernameAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(
                        400L,
                        "Пользователь с таким именем уже существует"
                )
        );
    }
}
