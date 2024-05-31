package ru.delmark.FunnyBot.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Aspect
public class ControllerLogAspect {


    @Pointcut("execution(public * ru.delmark.FunnyBot.controller.*.*(..))")
    public void callController() {}

    @Before("callController()")
    public void beforeCallController(JoinPoint joinPoint) {
        List<String> args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList();

        log.info("Called {} with args {}", joinPoint.getSignature().getName(), args);
    }

    @AfterReturning(value = "callController()", returning = "object")
    public void afterCallController(JoinPoint joinPoint, ResponseEntity<?> object) {
        log.info("Controller {} returned {}", joinPoint.getSignature().getName(), object.getBody());
    }
}
