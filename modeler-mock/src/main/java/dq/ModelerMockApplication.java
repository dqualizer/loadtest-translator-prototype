package dq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ModelerMockApplication {

	public static void main(String[] args) {
		SpringApplication.run(ModelerMockApplication.class, args);
		System.out.println("### MODELER MOCK STARTED ###");
	}
}