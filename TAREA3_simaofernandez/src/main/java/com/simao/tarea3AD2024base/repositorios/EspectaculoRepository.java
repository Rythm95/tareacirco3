package com.simao.tarea3AD2024base.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.modelo.Espectaculo;


@Repository
public interface EspectaculoRepository extends JpaRepository<Espectaculo, Long> {
	
	@Query("SELECT e FROM Espectaculo e LEFT JOIN FETCH e.numeros en LEFT JOIN FETCH en.numero WHERE e.id = :id")
	Optional<Espectaculo> findByIdWithNumeros(Long id);
	
	Espectaculo findByNombre(String nombre);
}
