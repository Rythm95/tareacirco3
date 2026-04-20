package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.repositorios.NumeroRepository;

@Service
public class NumeroService {

	@Autowired
	private NumeroRepository repo;

	public Numero save(Numero entity) {
		return repo.save(entity);
	}

	public Numero update(Numero entity) {
		return repo.save(entity);
	}

	public void delete(Numero entity) {
		repo.delete(null);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public Numero find(Long id) {
		return repo.findById(id).get();
	}

	public List<Numero> findAll() {
		return repo.findAll();
	}

	public void deleteInBatch(List<Numero> users) {
		repo.deleteAll(users);
	}

}
