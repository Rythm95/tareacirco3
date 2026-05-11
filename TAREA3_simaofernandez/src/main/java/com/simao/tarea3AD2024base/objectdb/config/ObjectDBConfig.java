package com.simao.tarea3AD2024base.objectdb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Configuration
public class ObjectDBConfig {

	private EntityManagerFactory emf;

	@Value("${objectdb.url}")
	private String url;

	@PostConstruct
	public void init() {
		emf = Persistence.createEntityManagerFactory(url);
	}

	public EntityManager createEntityManager() {
		return emf.createEntityManager();
	}

	@PreDestroy
	public void close() {
		if (emf != null && emf.isOpen()) {
			emf.close();
			System.out.println("ObjectDB cerrada correctamente.");
		}
	}

}
