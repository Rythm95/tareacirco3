package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.repositorios.CredsRepository;

@Service
public class CredsService {

	@Autowired
	private CredsRepository repo;

	public Credenciales save(Credenciales entity) {
		return repo.save(entity);
	}

	public Credenciales update(Credenciales entity) {
		return repo.save(entity);
	}

	public void delete(Credenciales entity) {
		repo.delete(entity);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public Credenciales find(Long id) {
		return repo.findById(id).get();
	}

	public List<Credenciales> findAll() {
		return repo.findAll();
	}

	public boolean authenticate(String username, String password) {
				
		Credenciales creds = this.findByUsername(username);
		if (creds == null) {
			return false;
		} else {
			if (password.equals(creds.getPassword()))
				return true;
			else
				return false;
		}
	}

	public Credenciales findByUsername(String user) {
		return repo.findByUsername(user);
	}
	
	public Credenciales findByPersona_Id(Long id) {
		 return repo.findByPersona_Id(id); 
	}

	public void deleteInBatch(List<Credenciales> users) {
		repo.deleteAll(users);
	}

}
