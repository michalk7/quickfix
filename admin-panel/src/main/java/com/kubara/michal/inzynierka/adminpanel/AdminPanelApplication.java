package com.kubara.michal.inzynierka.adminpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@SpringBootApplication(scanBasePackages = "com.kubara.michal.inzynierka.*")
public class AdminPanelApplication {

	@Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
	
	public static void main(String[] args) {
		SpringApplication.run(AdminPanelApplication.class, args);
	}

}
