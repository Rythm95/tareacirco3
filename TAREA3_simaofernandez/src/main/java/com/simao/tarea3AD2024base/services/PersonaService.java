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
import com.simao.tarea3AD2024base.modelo.TipoOperacion;
import com.simao.tarea3AD2024base.repositorios.PersonaRepository;

import jakarta.transaction.Transactional;

@Service
public class PersonaService {

	@Autowired
	private PersonaRepository repo;
	
	@Autowired
	private LogOperacionService loService;

	@Transactional
	public Persona save(Persona entity) {
		Persona saved = repo.save(entity);
		
		loService.newOperacion("Admin", TipoOperacion.NUEVO, "Se ha registrado una nueva persona de id "+saved.getId());
		
		return saved;
	}

	@Transactional
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
		
		loService.newOperacion("Admin", TipoOperacion.ACTUALIZACION, "Se ha actualiazado la información del coordinador de id "+oldId);

		return repo.save(ogCoord);
	}

	@Transactional
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

		loService.newOperacion("Admin",TipoOperacion.ACTUALIZACION, "Se ha actualiazado la información del artista de id "+oldId);
		
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

		nums.forEach(n -> {
			if (n.getEspectaculo() != null) {
				n.getEspectaculo().getNombre();
			}
		});
		a.getEspecialidades().size();

		return a;
	}

}
