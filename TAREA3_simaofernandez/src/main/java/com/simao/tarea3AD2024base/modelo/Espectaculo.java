package com.simao.tarea3AD2024base.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Espectaculo")
public class Espectaculo implements Comparable<Espectaculo> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	private Long idCoordinacion;
	
	@Column(unique = true)
	private String nombre;
	
	private LocalDate fechaini;
	
	private LocalDate fechafin;

	public Espectaculo(Long id, Long idCoordinacion, String nombre, LocalDate fechaini, LocalDate fechafin) {
		super();
		this.id = id;
		this.idCoordinacion = idCoordinacion;
		this.nombre = nombre;
		this.fechaini = fechaini;
		this.fechafin = fechafin;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdCoordinacion() {
		return idCoordinacion;
	}

	public void setIdCoordinacion(Long idCoordinacion) {
		this.idCoordinacion = idCoordinacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaini() {
		return fechaini;
	}

	public void setFechaini(LocalDate fechaini) {
		this.fechaini = fechaini;
	}

	public LocalDate getFechafin() {
		return fechafin;
	}

	public void setFechafin(LocalDate fechacfin) {
		this.fechafin = fechacfin;
	}

	@Override
	public String toString() {
		return "Espectáculo " + id + " - " + nombre + " [De " + fechaini + " a " + fechafin + "]";
	}

	@Override
	public int compareTo(Espectaculo o) {

		return Long.compare(this.id, o.id);
	}

}
