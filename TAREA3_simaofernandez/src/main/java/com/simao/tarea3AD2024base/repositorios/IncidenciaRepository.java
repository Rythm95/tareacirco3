package com.simao.tarea3AD2024base.repositorios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.config.ObjectDBManager;
import com.simao.tarea3AD2024base.objectdb.modelo.Incidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.ResolucionIncidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.TipoIncidencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class IncidenciaRepository {

	@Autowired
	private ObjectDBManager objectDB;

	public Incidencia save(Incidencia incidencia) {
		EntityManager em = objectDB.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			em.persist(incidencia);
			tx.commit();
			return incidencia;
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();

			throw e;

		} finally {
			em.close();
		}
	}

	public void solve(Incidencia incidencia, ResolucionIncidencia resolucion) {
		EntityManager em = objectDB.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();

			Incidencia inc = em.find(Incidencia.class, incidencia.getId());
			inc.setResuelta(true);
			em.merge(inc);

			em.persist(resolucion);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
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

	public List<Incidencia> findIncidenciaFiltro(TipoIncidencia tipo, Boolean resuelta, Long idEspectaculo,
			Long idNumero, LocalDate fechaIni, LocalDate fechaFin) {

		EntityManager em = objectDB.createEntityManager();

		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Incidencia> cq = cb.createQuery(Incidencia.class);
			Root<Incidencia> root = cq.from(Incidencia.class);

			List<Predicate> predicates = new ArrayList<>();

			if (tipo != null)
				predicates.add(cb.equal(root.get("tipo"), tipo));

			if (resuelta != null)
				predicates.add(cb.equal(root.get("resuelta"), resuelta));

			if (idEspectaculo != null)
				predicates.add(cb.equal(root.get("idEspectaculo"), idEspectaculo));

			if (idNumero != null)
				predicates.add(cb.equal(root.get("idNumero"), idNumero));

			if (fechaIni != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("fechaHora"), fechaIni.atStartOfDay()));

			if (fechaFin != null)
				predicates.add(cb.lessThanOrEqualTo(root.get("fechaHora"), fechaFin.atTime(23, 59, 59)));

			if (!predicates.isEmpty()) {
				cq.select(root).where(predicates.toArray(new Predicate[0]));
				return em.createQuery(cq).getResultList();
			} else {
				return findAll();
			}

		} finally {
			em.close();
		}

	}
}