package poly.foodease;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FoodEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodEaseApplication.class, args);
    }

}
