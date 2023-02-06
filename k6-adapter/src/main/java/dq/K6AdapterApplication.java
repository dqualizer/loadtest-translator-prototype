package dq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class K6AdapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(K6AdapterApplication.class, args);
		System.out.println("### k6 ADAPTER STARTED ###");
	}
}