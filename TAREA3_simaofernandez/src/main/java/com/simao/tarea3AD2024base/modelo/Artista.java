package com.simao.tarea3AD2024base.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Entity
@Table(name = "Artista")
public class Artista {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;

	private String apodo;

	private List<Especialidad> especialidades;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
