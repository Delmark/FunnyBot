package ru.delmark.FunnyBot.utils;

import jakarta.persistence.Column;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
public interface NowService {
    Date getCurrentDate();
}