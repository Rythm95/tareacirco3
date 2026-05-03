package com.simao.tarea3AD2024base.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.EspectaculoNumero;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.repositorios.EspectaculoRepository;

import jakarta.transaction.Transactional;

@Service
public class EspectaculoService {

	@Autowired
	private EspectaculoRepository repo;
	
	@Autowired
	private NumeroService nuService;

	public Espectaculo save(Espectaculo entity) {
		return repo.save(entity);
	}

	@Transactional
	public Espectaculo update(Long oldId, Espectaculo newEntity) {

		Espectaculo ogEntity = find(oldId);

		ogEntity.setNombre(newEntity.getNombre());
		ogEntity.setFechaini(newEntity.getFechaini());
		ogEntity.setFechafin(newEntity.getFechafin());
		ogEntity.setCoordinacion(newEntity.getCoordinacion());

		ogEntity.getNumeros().clear();

		for (EspectaculoNumero en : newEntity.getNumeros()) {
			en.setEspectaculo(ogEntity);
			ogEntity.getNumeros().add(en);
		}

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

		Espectaculo es = repo.findNumerosFromEspectaculo(id);

		return es.getNumeros().stream().map(EspectaculoNumero::getNumero).toList();
	}

	@Transactional
	public Espectaculo getEspectaculoCompleto(Long id) {
		Espectaculo es = repo.findEspectaculoCompleto(id);		
		List<Numero> nums = es.getNumeros().stream().map(EspectaculoNumero::getNumero).toList();
		
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
