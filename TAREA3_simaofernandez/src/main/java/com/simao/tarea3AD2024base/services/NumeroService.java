package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.modelo.TipoOperacion;
import com.simao.tarea3AD2024base.repositorios.NumeroRepository;

@Service
public class NumeroService {

	@Autowired
	private NumeroRepository repo;
	
	@Autowired
	private LogOperacionService loService;
	
	@Autowired
	private PersonaService peService;
	
	@Autowired
	private DossierArtisticoService daService;
	
	@Autowired
	private Session session;

	public Numero save(Numero entity) {
		Numero saved = repo.save(entity);
		
		String user = "Admin";
		if (session.getUserId() != null) {
			Persona p = peService.find(session.getUserId());
			user = p.getCredenciales().getUsername();
		}
		
		loService.newOperacion(user, TipoOperacion.NUEVO, "Se ha registrado un nuevo número de id "+saved.getId());
		
		return saved;
	}

	public Numero update(Long oldId, Numero newEntity) {

		Numero ogEntity = getNumeroWithArtistas(oldId);

		ogEntity.setNombre(newEntity.getNombre());
		ogEntity.setDuracion(newEntity.getDuracion());

		ogEntity.getArtistas().clear();

		for (Artista a : newEntity.getArtistas()) {
			daService.actualizarTrayectoria(a.getId());
			ogEntity.getArtistas().add(a);
		}

		String user = "Admin";
		if (session.getUserId() != null) {
			Persona p = peService.find(session.getUserId());
			user = p.getCredenciales().getUsername();
		}
		
		loService.newOperacion(user, TipoOperacion.ACTUALIZACION, "Se ha actualizado la información del número de id "+oldId);
		
		return repo.save(ogEntity);
	}

	public void delete(Numero entity) {
		repo.delete(entity);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public Numero find(Long id) {
		return repo.findById(id).get();
	}

	public Numero getNumeroWithArtistas(Long id) {
		return repo.findWithArtistas(id);
	}
	
	public List<Numero> getListArtistas(List<Numero> list){
		return repo.findListArtistas(list);
	}

	public Numero findByNombre(String nombre) {
		return repo.findByNombre(nombre);
	}
	
	public List<Numero> getNumerosWithEspectaculos(List<Numero> list){
		return repo.findNumerosWithEspectaculo(list);
	}

	public List<Numero> findAll() {
		return repo.findAll();
	}

	public void deleteInBatch(List<Numero> users) {
		repo.deleteAll(users);
	}

}
