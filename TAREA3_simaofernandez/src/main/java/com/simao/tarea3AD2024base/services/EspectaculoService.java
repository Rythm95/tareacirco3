package com.simao.tarea3AD2024base.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.modelo.TipoOperacion;
import com.simao.tarea3AD2024base.repositorios.EspectaculoRepository;

import jakarta.transaction.Transactional;

@Service
public class EspectaculoService {

	@Autowired
	private EspectaculoRepository repo;

	@Autowired
	private NumeroService nuService;

	@Autowired
	private PersonaService peService;

	@Autowired
	private LogOperacionService loService;

	@Autowired
	private Session session;

	@Transactional
	public Espectaculo save(Espectaculo entity, List<Long> numeroIds) {
		List<Numero> numeros = new ArrayList<>();

		for (int i = 0; i < numeroIds.size(); i++) {

			Numero n = nuService.find(numeroIds.get(i));

			n.setEspectaculo(entity);
			n.setOrden(i + 1);

			numeros.add(n);
		}

		for (Numero n : entity.getNumeros()) {
			n.setEspectaculo(null);
		}

		entity.setNumeros(numeros);

		Espectaculo saved = repo.save(entity);

		String user = "Admin";
		if (session.getUserId() != null) {
			Persona p = peService.find(session.getUserId());
			user = p.getCredenciales().getUsername();
		}

		loService.newOperacion(user, TipoOperacion.NUEVO,
				"Se ha registrado un nuevo espectáculo de id " + saved.getId());

		return saved;
	}

	@Transactional
	public Espectaculo update(Long oldId, Espectaculo newEntity, List<Long> numIds) {

		Espectaculo ogEntity = find(oldId);

		ogEntity.setNombre(newEntity.getNombre());
		ogEntity.setFechaini(newEntity.getFechaini());
		ogEntity.setFechafin(newEntity.getFechafin());
		ogEntity.setCoordinacion(newEntity.getCoordinacion());

		for (Numero n : ogEntity.getNumeros()) {
			n.setEspectaculo(null);
		}

		ogEntity.getNumeros().clear();

		for (int i = 0; i < numIds.size(); i++) {

			Numero n = nuService.find(numIds.get(i));

			n.setEspectaculo(ogEntity);
			n.setOrden(i + 1);

			ogEntity.getNumeros().add(n);
		}

		String user = "Admin";
		if (session.getUserId() != null) {
			Persona p = peService.find(session.getUserId());
			user = p.getCredenciales().getUsername();
		}

		loService.newOperacion(user, TipoOperacion.ACTUALIZACION,
				"Se ha actualizado la información del espectáculo de id " + oldId);

		return repo.save(ogEntity);
	}

	public void delete(Espectaculo entity) {
		repo.delete(entity);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public Espectaculo find(Long id) {
		return repo.findById(id).get();
	}

	public List<Numero> getNumerosFromEspectaculo(Long id) {

		Espectaculo es = repo.findEspectaculoCompleto(id);

		return es.getNumeros();
	}

	@Transactional
	public Espectaculo getEspectaculoCompleto(Long id) {
		Espectaculo es = repo.findEspectaculoCompleto(id);
		List<Numero> nums = es.getNumeros();

		List<Numero> numerosArtistas = nuService.getListArtistas(nums);

		numerosArtistas.forEach(n -> n.getArtistas().forEach(a -> a.getEspecialidades().size()));

		return es;
	}

	public List<Espectaculo> findAll() {
		return repo.findAll();
	}

	public Espectaculo findByNombre(String nombre) {
		return repo.findByNombre(nombre);
	}

	public void deleteInBatch(List<Espectaculo> users) {
		repo.deleteAll(users);
	}

}
