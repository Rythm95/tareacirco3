package com.simao.tarea3AD2024base.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.modelo.Numero;

@Repository
public interface NumeroRepository extends JpaRepository<Numero, Long> {

	@Query("SELECT n FROM Numero n LEFT JOIN FETCH n.artistas WHERE n.id = :id")
	Numero findWithArtistas(Long id);

	@Query("SELECT DISTINCT n FROM Numero n LEFT JOIN FETCH n.artistas WHERE n IN :numeros")
	List<Numero> findListArtistas(List<Numero> numeros);
	
	@Query("SELECT DISTINCT n FROM Numero n LEFT JOIN FETCH n.espectaculos en LEFT JOIN FETCH en.espectaculo WHERE n IN :numeros")
	List<Numero> findNumerosWithEspectaculos(List<Numero> numeros);

	Numero findByNombre(String nombre);
}
