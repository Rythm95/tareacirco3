package com.simao.tarea3AD2024base.objectdb.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// @Configuration
public class ObjectDBConfig {/*

	@Value("${objectdb.url}")
	private String url;

	@Value("${objectdb.user}")
	private String user;

	@Value("${objectdb.password}")
	private String password;

	
	@Bean(name = "objectdbEmfProvider")
	public ObjectDBEmfProvider objectdbEmfProvider() {

		com.objectdb.jpa.Provider provider = new com.objectdb.jpa.Provider();

		Map<String, Object> props = new HashMap<>();
		props.put("jakarta.persistence.jdbc.url", url);
		props.put("jakarta.persistence.jdbc.user", user);
		props.put("jakarta.persistence.jdbc.password", password);

		com.objectdb.jpa.EMF emf = (com.objectdb.jpa.EMF) provider.createEntityManagerFactory(
		        "objectdbPU", props
		    );

		return new ObjectDBEmfProvider(emf);
	}

	@Bean(name = "objectdbTransactionManager")
	public PlatformTransactionManager objectdbTransactionManager(
			@Qualifier("objectdbEmfProvider") ObjectDBEmfProvider provider) {
		return new JpaTransactionManager(provider.getEmf());
	}
	*/
}
