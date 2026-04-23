package com.simao.tarea3AD2024base.modelo;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Entity
public class Artista extends Persona {

	private String apodo;

	@ElementCollection
    @Enumerated(EnumType.STRING)
	private List<Especialidad> especialidades;

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	public List<Especialidad> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}

	// Convierte las especialidades a una cadena de texto, separandolas por comas.
	public String especialidadesToString() {
		return especialidades.stream().map(Enum::name).reduce((a, b) -> a + "," + b).orElse("");
	}

}
