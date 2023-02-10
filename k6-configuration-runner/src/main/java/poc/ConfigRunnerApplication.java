package poc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigRunnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigRunnerApplication.class, args);
		System.out.println("##### k6 CONFIGURATION RUNNER STARTED #####");
	}
}