package ru.delmark.FunnyBot.aop;

import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class TelegramBotServiceAspect {

    @Pointcut("execution(public * ru.delmark.FunnyBot.service.TelegramBotService.handleUpdate(..))")
    public void handleUpdatePointcut() {}

    // Не работает?
    @Before("handleUpdatePointcut()")
    public void logUpdateDetails(JoinPoint joinPoint) {
        Update update = (Update) Arrays.stream(joinPoint.getArgs()).findFirst().orElse(null);

        if (update.message() != null) {
            log.info("Received Telegram update: ID={}, ChatID={}, Username={}",
                    update.updateId(),
                    update.message().chat().id(),
                    update.message().from().username());
        } else {
            log.info("Received Telegram update: ID={}", update.updateId());
        }
    }
}


