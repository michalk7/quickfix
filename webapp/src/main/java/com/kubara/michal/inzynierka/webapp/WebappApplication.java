package com.kubara.michal.inzynierka.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.kubara.michal.inzynierka.*")
@EnableJpaRepositories(basePackages = "com.kubara.michal.inzynierka.*")
@EntityScan(basePackages = "com.kubara.michal.inzynierka.*")
public class WebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}

}
