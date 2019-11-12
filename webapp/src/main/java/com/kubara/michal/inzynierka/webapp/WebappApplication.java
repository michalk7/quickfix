package com.kubara.michal.inzynierka.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication(scanBasePackages = "com.kubara.michal.inzynierka.*")
@EnableJpaRepositories(basePackages = "com.kubara.michal.inzynierka.*")
@EntityScan(basePackages = "com.kubara.michal.inzynierka.*")
@EnableAsync
public class WebappApplication {

	@Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}

}
