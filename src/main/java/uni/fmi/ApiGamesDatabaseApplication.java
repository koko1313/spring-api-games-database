package uni.fmi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ApiGamesDatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGamesDatabaseApplication.class, args);
	}
	
	/**
	 * The password encoder to use when encrypting passwords.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
