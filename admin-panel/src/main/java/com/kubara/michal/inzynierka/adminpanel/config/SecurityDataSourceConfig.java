package com.kubara.michal.inzynierka.adminpanel.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = {"${security.data.jpa.repository.packages}"},
		entityManagerFactoryRef = "entityManagerFactorySecurity",
		transactionManagerRef = "securityTransactionManager")
public class SecurityDataSourceConfig {

	@Autowired
	private Environment environment;
	
	@Bean
	@ConfigurationProperties(prefix="security.datasource")
	public DataSource securityDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@ConfigurationProperties(prefix="security.data.jpa.entity")
	public LocalContainerEntityManagerFactoryBean entityManagerFactorySecurity(EntityManagerFactoryBuilder builder, 
			@Qualifier("securityDataSource") DataSource securityDataSource) {
		LocalContainerEntityManagerFactoryBean factoryBean = builder.dataSource(securityDataSource).build();
		factoryBean.getJpaPropertyMap().put("hibernate.hbm2ddl.auto", environment.getProperty("security.jpa.hibernate.dll-auto"));
		
		return factoryBean;
	}
	
	@Bean
	public PlatformTransactionManager securityTransactionManager(
			@Qualifier("entityManagerFactorySecurity") EntityManagerFactory entityManagerFactorySecurity) {
		return new JpaTransactionManager(entityManagerFactorySecurity);
	}
	
}
