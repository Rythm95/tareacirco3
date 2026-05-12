package com.simao.tarea3AD2024base.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ObjectDBManager {

    private EntityManagerFactory emf;

    @Value("${objectdb.url}")
    private String objectdbUrl;

    @PostConstruct
    public void init() {
        emf = Persistence.createEntityManagerFactory(objectdbUrl);
    }

    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }

    @PreDestroy
    public void close() {
        if (emf != null && emf.isOpen())
            emf.close();
    }
}