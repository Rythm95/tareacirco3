package com.simao.tarea3AD2024base.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Numero")
public class Numero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(unique = true)
	private String nombre;

	private double duracion;

	private int orden;

	@ManyToOne
	@JoinColumn(name = "espectaculo_id")
	private Espectaculo espectaculo;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "numero_artista", joinColumns = @JoinColumn(name = "numero_id"), inverseJoinColumns = @JoinColumn(name = "artista_id"))
	private List<Artista> artistas;

	public Numero() {
	}

	public Numero(Long id, String nombre, double duracion, int orden, Espectaculo espectaculo, List<Artista> artistas) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.duracion = duracion;
		this.orden = orden;
		this.espectaculo = espectaculo;
		this.artistas = artistas;
	}

	public Numero(Long id, String nombre, double duracion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.duracion = duracion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<Artista> getArtistas() {
		return artistas;
	}

	public void setArtistas(List<Artista> artistas) {
		this.artistas = artistas;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public Espectaculo getEspectaculo() {
		return espectaculo;
	}

	public void setEspectaculo(Espectaculo espectaculo) {
		this.espectaculo = espectaculo;
	}

	public String toString() {
		return "[id " + id + "]\tNombre: " + nombre + "\tDuración: " + duracion + " min";
	}

}
