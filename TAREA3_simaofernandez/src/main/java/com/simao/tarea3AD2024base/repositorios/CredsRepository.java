package com.simao.tarea3AD2024base.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simao.tarea3AD2024base.modelo.Credenciales;


@Repository
public interface CredsRepository extends JpaRepository<Credenciales, Long> {

	Credenciales findByUsername(String user);
}
