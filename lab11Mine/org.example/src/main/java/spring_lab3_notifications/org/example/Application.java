package spring_lab3_notifications.org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import spring_lab3_notifications.org.example.config.AppConfig;
import spring_lab3_notifications.org.example.config.AnotherConfig;

@SpringBootApplication
@Import(AppConfig.class)
//@Import(AnotherConfig.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
