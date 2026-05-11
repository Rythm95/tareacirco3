package com.simao.tarea3AD2024base.objectdb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simao.tarea3AD2024base.objectdb.config.ObjectDBEmfProvider;
import com.simao.tarea3AD2024base.objectdb.modelo.Incidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.ResolucionIncidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.TipoIncidencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

//@Service
public class IncidenciaService {/*

	@Autowired
	@Qualifier("objectdbEmfProvider")
	private ObjectDBEmfProvider  emfProvider;

	private EntityManager em() {
		return emfProvider.createEntityManager();
	}

	@Transactional("objectdbTransactionManager")
	public void registrarIncidencia(TipoIncidencia tipo, String descripcion, Long idPersona, Long idEspectaculo,
			Long idNumero) {

		EntityManager em = em();
		Incidencia i = new Incidencia();

		i.setFechaHora(LocalDateTime.now().toString());
		i.setTipo(tipo);
		i.setDescripcion(descripcion);
		i.setResuelta(false);

		i.setIdPersonaReporta(idPersona);

		i.setIdEspectaculo(idEspectaculo);
		i.setIdNumero(idNumero);

		em.getTransaction().begin();
		em.persist(i);
		em.getTransaction().commit();
		em.close();
	}

	@Transactional("objectdbTransactionManager")
	public void resolverIncidencia(Long idIncidencia, String acciones, Long idPersonaResuelve) {

		EntityManager em = em();
		em.getTransaction().begin();

		Incidencia i = em.find(Incidencia.class, idIncidencia);
		i.setResuelta(true);

		ResolucionIncidencia ri = new ResolucionIncidencia();
		ri.setFechaHoraResolucion(LocalDateTime.now().toString());
		ri.setAccionesRealizadas(acciones);
		ri.setIdPersonaResuelve(idPersonaResuelve);

		ri.setIncidencia(i);

		em.persist(ri);
		em.merge(i);
		em.getTransaction().commit();
		em.close();
	}

	@Transactional("objectdbTransactionManager")
	public List<Incidencia> buscarIncidencias(TipoIncidencia tipo, Boolean resuelta, Long idEspectaculo, Long idNumero,
			LocalDate fechaIni, LocalDate fechaFin) {

		EntityManager em = em();
		String jpql = "SELECT i FROM Incidencia i WHERE 1=1";

		if (tipo != null)
			jpql += " AND i.tipo = :tipo";

		if (resuelta != null)
			jpql += " AND i.resuelta = :resuelta";

		if (idEspectaculo != null)
			jpql += " AND i.idEspectaculo = :idEspectaculo";

		if (idNumero != null) {
			jpql += " AND i.idNumero = :idNumero";
		}

		TypedQuery<Incidencia> query = em.createQuery(jpql, Incidencia.class);

		if (tipo != null)
			query.setParameter("tipo", tipo);

		if (resuelta != null)
			query.setParameter("resuelta", resuelta);

		if (idEspectaculo != null)
			query.setParameter("idEspectaculo", idEspectaculo);

		if (idNumero != null)
			query.setParameter("idNumero", idNumero);

		List<Incidencia> resultado = query.getResultList();
		em.close();

		if (fechaIni != null || fechaFin != null) {

			resultado = resultado.stream().filter(i -> {

				LocalDate fecha = LocalDate.parse(i.getFechaHora().substring(0, 10));

				boolean ok = true;

				if (fechaIni != null)
					ok &= !fecha.isBefore(fechaIni);

				if (fechaFin != null)
					ok &= !fecha.isAfter(fechaFin);

				return ok;
			}).toList();
		}

		return resultado;
	}

	@Transactional("objectdbTransactionManager")
	public List<Incidencia> findAll() {
		EntityManager em = em();
		List<Incidencia> lista = em.createQuery("SELECT i FROM Incidencia i ORDER BY i.fechaHora DESC", Incidencia.class).getResultList();
		em.close();
		return lista;
	}
*/}
