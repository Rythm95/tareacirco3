package com.simao.tarea3AD2024base.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class Espectaculo implements Comparable<Espectaculo> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)	
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	private LocalDate fechaini;
	private LocalDate fechafin;
	
	@ManyToOne
	@JoinColumn(name = "id_coordinacion")
	private Coordinacion coordinacion;
	
	@OneToMany(mappedBy = "espectaculo", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("orden ASC")
	private List<EspectaculoNumero> numeros = new ArrayList<>();
	
	public Espectaculo() {}
	
	public Espectaculo(Long id, Coordinacion coordinacion, String nombre, LocalDate fechaini, LocalDate fechafin) {
		super();
		this.id = id;
		this.coordinacion = coordinacion;
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

	public Coordinacion getCoordinacion() {
		return coordinacion;
	}

	public void setCoordinacion(Coordinacion coordinacion) {
		this.coordinacion = coordinacion;
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

	public List<EspectaculoNumero> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<EspectaculoNumero> numeros) {
		this.numeros = numeros;
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
