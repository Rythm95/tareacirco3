package com.simao.tarea3AD2024base.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.modelo.mongo.DossierArtistico;
import com.simao.tarea3AD2024base.modelo.mongo.Evaluacion;
import com.simao.tarea3AD2024base.modelo.mongo.Evaluador;
import com.simao.tarea3AD2024base.modelo.mongo.NumeroTrayectoria;
import com.simao.tarea3AD2024base.modelo.mongo.Observacion;
import com.simao.tarea3AD2024base.modelo.mongo.Trayectoria;
import com.simao.tarea3AD2024base.repositorios.mongo.DossierRepository;

import jakarta.transaction.Transactional;

@Service
public class DossierArtisticoService {

	@Autowired
	private DossierRepository repo;

	@Autowired
	private PersonaService peService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private Session session;

	@Transactional
	public void guardarDossierArtista(Long idArtista) {

		Artista artista = peService.findArtistaCompleto(idArtista);

		DossierArtistico dossier = findByArtista(idArtista).orElseGet(() -> {
			DossierArtistico d = new DossierArtistico();
			d.setIdArtista(idArtista);
			d.setTrayectoria(new ArrayList<>());
			return d;
		});

		dossier.setIdArtista(artista.getId());
		dossier.setNombre(artista.getNombre());
		dossier.setApodo(artista.getApodo());
		dossier.setEmail(artista.getEmail());
		dossier.setNacionalidad(artista.getNacionalidad());
		dossier.setEspecialidades(artista.getEspecialidades().stream().map(Enum::name).toList());

		repo.save(dossier);
	}

	@Transactional
	public void actualizarDossierDatosPersonales(Long idArtista) {

		Artista artista = peService.findArtistaCompleto(idArtista);

		Query query = Query.query(Criteria.where("idArtista").is(idArtista));

		Update update = new Update().set("nombre", artista.getNombre()).set("apodo", artista.getApodo())
				.set("email", artista.getEmail()).set("nacionalidad", artista.getNacionalidad())
				.set("especialidades", artista.getEspecialidades().stream().map(Enum::name).toList());

		mongoTemplate.upsert(query, update, DossierArtistico.class);
	}

	@Transactional
	public void actualizarDossierArtista(Long idArtista, String comentarioEvaluacion, String nivel,
			String observacionTexto) {

		Query query = Query.query(Criteria.where("idArtista").is(idArtista));
		Update update = new Update();
		boolean cambios = false;

		if (comentarioEvaluacion != null && !comentarioEvaluacion.isBlank()) {

			Evaluacion ev = new Evaluacion();

			ev.setFecha(LocalDate.now().toString());

			Evaluador evaluador = new Evaluador();
			evaluador.setIdPersona(session.getUserId());
			if (session.getUserId() != null) {
				evaluador.setRol(Perfil.COORDINACION.toString());
			} else {
				evaluador.setRol("ADMIN");
			}

			ev.setRealizadaPor(evaluador);
			ev.setComentario(comentarioEvaluacion);
			ev.setNivel(nivel);

			update.push("evaluaciones", ev);
			cambios = true;
		}

		if (observacionTexto != null && !observacionTexto.isBlank()) {

			Observacion obs = new Observacion();

			obs.setFecha(LocalDate.now().toString());
			obs.setTexto(observacionTexto);
			if (session.getUserId() != null)
				obs.setAutor(peService.find(session.getUserId()).getCredenciales().getUsername());
			else
				obs.setAutor("Admin");

			update.push("observaciones", obs);
			cambios = true;
		}

		if (cambios)
			mongoTemplate.upsert(query, update, DossierArtistico.class);
	}

	@Transactional
	public void actualizarTrayectoria(Long idArtista) {
		Artista artista = peService.findArtistaCompleto(idArtista);

		List<Trayectoria> trayectoria = getTrayectoria(artista);

		Query query = Query.query(Criteria.where("idArtista").is(idArtista));
		Update update = new Update().set("trayectoria", trayectoria);

		mongoTemplate.upsert(query, update, DossierArtistico.class);
	}

	private List<Trayectoria> getTrayectoria(Artista artista) {
		List<Trayectoria> trayectorias = new ArrayList<>();
		Map<Long, Trayectoria> mapa = new HashMap<>();

		for (Numero numero : artista.getNumeros()) {
			Espectaculo es = numero.getEspectaculo();

			if (es == null) {
				continue;
			}

			Trayectoria tray = mapa.get(es.getId());

			if (tray == null) {

				tray = new Trayectoria();
				tray.setIdEspectaculo(es.getId());
				tray.setNombreEspectaculo(es.getNombre());
				tray.setNumeros(new ArrayList<>());

				mapa.put(es.getId(), tray);
				trayectorias.add(tray);
			}

			NumeroTrayectoria nt = new NumeroTrayectoria();

			nt.setIdNumero(numero.getId());
			nt.setNombreNumero(numero.getNombre());

			tray.getNumeros().add(nt);
		}

		return trayectorias;
	}

	public Optional<DossierArtistico> findByArtista(Long artistaId) {
		return repo.findByIdArtista(artistaId);
	}

}
