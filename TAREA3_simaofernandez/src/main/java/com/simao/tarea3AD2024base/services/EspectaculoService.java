package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.EspectaculoNumero;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.repositorios.EspectaculoRepository;

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
	
	@Transactional(readOnly = true)
	public List<Numero> getNumerosFromEspectaculo(Long id) {
		
		Espectaculo es = repo.findWithNumeros(id);
		
		return es.getNumeros().stream().map(EspectaculoNumero::getNumero).toList();
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
