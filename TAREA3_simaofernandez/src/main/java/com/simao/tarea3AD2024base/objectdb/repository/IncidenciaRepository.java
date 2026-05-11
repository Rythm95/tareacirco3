/**
* Clase IncidenciaRepository.java
*
* @author Simao Fernandez Gervasoni
* @version 1.0
*/
package com.simao.tarea3AD2024base.objectdb.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.objectdb.config.ObjectDBConfig;
import com.simao.tarea3AD2024base.objectdb.modelo.Incidencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

@Repository
public class IncidenciaRepository {

	@Autowired
	private ObjectDBConfig objectDB;

	public Incidencia save(Incidencia incidencia) {

		EntityManager em = objectDB.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {

			tx.begin();

			if (incidencia.getId() == null) {
				em.persist(incidencia);
			} else {
				incidencia = em.merge(incidencia);
			}

			tx.commit();

			return incidencia;

		} catch (Exception e) {

			if (tx.isActive()) {
				tx.rollback();
			}

			throw e;

		} finally {

			em.close();
		}
	}

	public Incidencia find(Long id) {
		EntityManager em = objectDB.createEntityManager();

		try {

			return em.find(Incidencia.class, id);

		} finally {

			em.close();
		}
	}

	public List<Incidencia> findAll() {

		EntityManager em = objectDB.createEntityManager();

		try {

			TypedQuery<Incidencia> q = em.createQuery("SELECT i FROM Incidencia i", Incidencia.class);

			return q.getResultList();

		} finally {

			em.close();
		}
	}

}
