package dq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoadtestTranslatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadtestTranslatorApplication.class, args);
		System.out.println("### LOADTEST TRANSLATOR STARTED ###");
	}
}