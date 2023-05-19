package backend.section6mainproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
@EnableJpaAuditing
public class Section6MainProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Section6MainProjectApplication.class, args);
	}
}
