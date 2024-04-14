package utils;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Component
public class NowServiceImpl implements NowService {
    @Override
    public Date getCurrentDate() {
        return new Date();
    }

}