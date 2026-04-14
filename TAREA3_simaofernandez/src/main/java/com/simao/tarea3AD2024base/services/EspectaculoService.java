package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.repositorios.EspectaculoRepository;

import modelo.Espectaculo;

@Service
public class EspectaculoService {

	@Autowired
	private EspectaculoRepository repo;

	public Espectaculo save(Espectaculo entity) {
		return repo.save(entity);
	}

	public Espectaculo update(Espectaculo entity) {
		return repo.save(entity);
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

	public List<Espectaculo> findAll() {
		return repo.findAll();
	}

	public void deleteInBatch(List<Espectaculo> users) {
		repo.deleteAll(users);
	}

}
