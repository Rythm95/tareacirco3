package com.simao.tarea3AD2024base.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.objectdb.modelo.Incidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.ResolucionIncidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.TipoIncidencia;
import com.simao.tarea3AD2024base.repositorios.IncidenciaRepository;

@Service
public class IncidenciaService {

	@Autowired
	private IncidenciaRepository repo;
	
	@Autowired
	private Session session;

	public void registrar(TipoIncidencia tipo, String descripcion, Long idEspectaculo, Long idNumero) {
		Long idPersona = session.getUserId();
		Incidencia in = new Incidencia(LocalDateTime.now(), tipo, descripcion, idPersona, idEspectaculo, idNumero);
		repo.save(in);
	}

	public void resolver(Incidencia incidencia, String accionesRealizadas) {
		Long idPersona = session.getUserId();
		ResolucionIncidencia resolucion = new ResolucionIncidencia(LocalDateTime.now(), accionesRealizadas, idPersona,
				incidencia);
		repo.solve(incidencia, resolucion);
	}

	public List<Incidencia> findAll() {
		return repo.findAll();
	}

	public List<Incidencia> findIncidenciaFiltro(TipoIncidencia tipo, Boolean resuelta, Long idEspectaculo,
			Long idNumero, LocalDate fechaIni, LocalDate fechaFin) {
		return repo.findIncidenciaFiltro(tipo, resuelta, idEspectaculo, idNumero, fechaIni, fechaFin);
	}
}