package poly.foodease.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoggingService {
    private static final Logger logger = LoggerFactory.getLogger(LoggingService.class);

    public void performTask() {
        logger.debug("This is a DEBUG message");
        logger.info("This is an INFO message");
        logger.error("This is an ERROR message");
    }
}
