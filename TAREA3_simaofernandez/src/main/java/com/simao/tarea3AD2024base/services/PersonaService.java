package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.repositorios.PersonaRepository;

@Service
public class PersonaService {

	@Autowired
	private PersonaRepository repo;

	public Persona save(Persona entity) {
		return repo.save(entity);
	}

	public Persona update(Persona entity) {
		return repo.save(entity);
	}

	public void delete(Persona entity) {
		repo.delete(null);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public Persona find(Long id) {
		return repo.findById(id).get();
	}

	public List<Persona> findAll() {
		return repo.findAll();
	}

	public void deleteInBatch(List<Persona> users) {
		repo.deleteAll(users);
	}

	public List<Persona> findByPerfil(Perfil perfil) {
		return repo.findByPerfil(perfil);
	}
	
	public Persona findByNombre(String nombre) {
		return repo.findByNombre(nombre);
	}
	
	public Persona findByEmail (String email) {
		return repo.findByEmail(email);
	}

}
