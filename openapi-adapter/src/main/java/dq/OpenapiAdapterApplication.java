package dq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OpenapiAdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenapiAdapterApplication.class, args);
		System.out.println("### OPENAPI ADAPTER STARTED ###");
	}
}