package com.simao.tarea3AD2024base.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

	@Query("SELECT p FROM Persona p WHERE p.credenciales.perfil = :perfil")
	List<Persona> findByPerfil(@Param("perfil") Perfil perfil);

}
