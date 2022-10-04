package dev.saha.otpauthserverweb;

import dev.saha.otpauthserverweb.service.impl.StartUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
@RequiredArgsConstructor
public class OtpAuthServerWebApplication  implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(OtpAuthServerWebApplication.class, args);
	}

	private final StartUpService start;


	@Override
	public void run(String... args) throws Exception {
		start.createAdmin();
	}
}
