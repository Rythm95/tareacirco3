package com.simao.tarea3AD2024base.repositorios.mongo;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.simao.tarea3AD2024base.modelo.mongo.DossierArtistico;

public interface DossierRepository extends MongoRepository<DossierArtistico, String> {
	
	Optional<DossierArtistico> findByIdArtista(Long idArtista);
}
