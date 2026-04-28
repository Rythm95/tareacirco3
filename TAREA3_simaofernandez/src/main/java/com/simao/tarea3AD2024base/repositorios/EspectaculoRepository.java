package com.simao.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.modelo.Espectaculo;


@Repository
public interface EspectaculoRepository extends JpaRepository<Espectaculo, Long> {
	
	Espectaculo findByNombre(String nombre);
}
