package com.simao.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.modelo.Numero;


@Repository
public interface NumeroRepository extends JpaRepository<Numero, Long> {
	
	@Query("SELECT n FROM Numero n LEFT JOIN FETCH n.artistas WHERE n.id = :id")
	Numero findWithArtistas(Long id);
	
	Numero findByNombre(String nombre);
}
