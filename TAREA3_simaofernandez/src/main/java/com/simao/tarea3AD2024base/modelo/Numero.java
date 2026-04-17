package com.simao.tarea3AD2024base.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Numero")
public class Numero {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	
	private int orden;
	
	@Column(unique = true)
	private String nombre;
	
	private double duracion;
	
	private Long idEspec;

	public Numero() {}
	
	public Numero(Long id, int orden, String nombre, double duracion, Long idEspec) {
		super();
		this.id = id;
		this.orden = orden;
		this.nombre = nombre;
		this.duracion = duracion;
		this.idEspec = idEspec;
	}
	
	public Numero(Long id, String nombre, double duracion) {
		super();
		this.id = id;
		this.orden = 0;
		this.nombre = nombre;
		this.duracion = duracion;
		this.idEspec = null;
	}

	public Long getIdEspec() {
		return idEspec;
	}

	public void setIdEspec(Long idEspec) {
		this.idEspec = idEspec;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getDuracion() {
		return duracion;
	}

	public void setDuracion(double duracion) {
		this.duracion = duracion;
	}

	public String toString() {
		return "Id: "+id+"\tNombre: "+nombre+"\tDuración: "+duracion + " min";
	}
	
}
