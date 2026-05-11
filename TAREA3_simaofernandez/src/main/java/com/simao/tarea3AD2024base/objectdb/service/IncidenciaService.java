package com.simao.tarea3AD2024base.objectdb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simao.tarea3AD2024base.objectdb.modelo.Incidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.ResolucionIncidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.TipoIncidencia;
import com.simao.tarea3AD2024base.objectdb.repository.IncidenciaRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class IncidenciaService {

	
	@Autowired
	private IncidenciaRepository repo;
	
	public Incidencia save(Incidencia i) {
        return repo.save(i);
    }

    public List<Incidencia> findAll() {
        return repo.findAll();
    }

	@Transactional("objectdbTransactionManager")
	public void registrarIncidencia(TipoIncidencia tipo, String descripcion, Long idPersona, Long idEspectaculo,
			Long idNumero) {

		Incidencia i = new Incidencia();

		i.setFechaHora(LocalDateTime.now().toString());
		i.setTipo(tipo);
		i.setDescripcion(descripcion);
		i.setResuelta(false);

		i.setIdPersonaReporta(idPersona);

		i.setIdEspectaculo(idEspectaculo);
		i.setIdNumero(idNumero);

		em.persist(i);
	}

	@Transactional("objectdbTransactionManager")
	public void resolverIncidencia(Long idIncidencia, String acciones, Long idPersonaResuelve) {

		Incidencia i = em.find(Incidencia.class, idIncidencia);

		i.setResuelta(true);

		ResolucionIncidencia r = new ResolucionIncidencia();

		r.setFechaHoraResolucion(LocalDateTime.now().toString());
		r.setAccionesRealizadas(acciones);
		r.setIdPersonaResuelve(idPersonaResuelve);

		r.setIncidencia(i);

		em.persist(r);

		em.merge(i);
	}

	@Transactional("objectdbTransactionManager")
	public List<Incidencia> buscarIncidencias(TipoIncidencia tipo, Boolean resuelta, Long idEspectaculo, Long idNumero,
			LocalDate fechaIni, LocalDate fechaFin) {

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
			predicates.add(cb.greaterThanOrEqualTo(cb.substring(root.get("fechaHora"), 1, 10), fechaIni.toString()));

		if (fechaFin != null)
			predicates.add(cb.lessThanOrEqualTo(cb.substring(root.get("fechaHora"), 1, 10), fechaFin.toString()));

		cq.select(root).where(predicates.toArray(new Predicate[0])).orderBy(cb.desc(root.get("id")));

		return em.createQuery(cq).getResultList();

	}
}
