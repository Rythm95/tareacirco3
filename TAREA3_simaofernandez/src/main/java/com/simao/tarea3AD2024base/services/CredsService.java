package com.simao.tarea3AD2024base.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.repositorios.CredsRepository;

@Service
public class CredsService {

	@Autowired
	private CredsRepository credsRepository;

	public Credenciales save(Credenciales entity) {
		return credsRepository.save(entity);
	}

	public Credenciales update(Credenciales entity) {
		return credsRepository.save(entity);
	}

	public void delete(Credenciales entity) {
		credsRepository.delete(entity);
	}

	public void delete(Long id) {
		credsRepository.deleteById(id);
	}

	public Credenciales find(Long id) {
		return credsRepository.findById(id).get();
	}

	public List<Credenciales> findAll() {
		return credsRepository.findAll();
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
		return credsRepository.findByUsername(user);
	}

	public void deleteInBatch(List<Credenciales> users) {
		credsRepository.deleteAll(users);
	}

}
