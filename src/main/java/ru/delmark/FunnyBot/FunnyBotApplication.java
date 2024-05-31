package ru.delmark.FunnyBot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.delmark.FunnyBot.model.Role;
import ru.delmark.FunnyBot.repository.RoleRepository;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableTransactionManagement
public class FunnyBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(FunnyBotApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByAuthority("USER").isPresent()) {
				return;
			}

			roleRepository.save(new Role(null, "USER"));
			roleRepository.save(new Role(null, "MODERATOR"));
			roleRepository.save(new Role(null, "ADMIN"));
		};
	}

}
