package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.repositorios.PersonaRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonaService {

	@Autowired
	private PersonaRepository repo;

	@Autowired
	private NumeroService nuService;

	public Persona save(Persona entity) {
		return repo.save(entity);
	}

	public Persona updateCoordinacion(Long oldId, Coordinacion newEntity, Credenciales newCreds) {
		Coordinacion ogCoord = (Coordinacion) find(oldId);
		Credenciales ogCreds = ogCoord.getCredenciales();

		ogCoord.setNombre(newEntity.getNombre());
		ogCoord.setEmail(newEntity.getEmail());
		ogCoord.setNacionalidad(newEntity.getNacionalidad());

		ogCoord.setSenior(newEntity.isSenior());
		if (newEntity.isSenior())
			ogCoord.setFechaSenior(newEntity.getFechaSenior());
		else
			ogCoord.setFechaSenior(null);

		ogCreds.setUsername(newCreds.getUsername());
		ogCreds.setPassword(newCreds.getPassword());
		ogCreds.setPerfil(newCreds.getPerfil());
		ogCreds.setPersona(ogCoord);
		ogCoord.setCredenciales(ogCreds);

		return repo.save(ogCoord);
	}

	public Persona updateArtista(Long oldId, Artista newEntity, Credenciales newCreds) {
		Artista ogArt = (Artista) findArtistaEspecialidades(oldId);
		Credenciales ogCreds = ogArt.getCredenciales();

		ogArt.setNombre(newEntity.getNombre());
		ogArt.setEmail(newEntity.getEmail());
		ogArt.setNacionalidad(newEntity.getNacionalidad());

		ogArt.setApodo(newEntity.getApodo());
		ogArt.getEspecialidades().clear();
		ogArt.getEspecialidades().addAll(newEntity.getEspecialidades());

		ogCreds.setUsername(newCreds.getUsername());
		ogCreds.setPassword(newCreds.getPassword());
		ogCreds.setPerfil(newCreds.getPerfil());
		ogCreds.setPersona(ogArt);
		ogArt.setCredenciales(ogCreds);

		return repo.save(ogArt);
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

	public Persona findByEmail(String email) {
		return repo.findByEmail(email);
	}

	public Persona findArtistaEspecialidades(Long id) {
		return repo.findWithEspecialidades(id);
	}

	@Transactional
	public Artista findArtistaCompleto(Long id) {
		Artista a = (Artista) repo.findArtistaWithNumeros(id);

		List<Numero> nums = a.getNumeros();
		List<Numero> numsFull = nuService.getNumerosWithEspectaculos(nums);

		numsFull.forEach(n -> n.getEspectaculos().forEach(en -> en.getEspectaculo().getNombre()));
		a.getEspecialidades().size();
		
		return a;
	}

}
