package com.simao.tarea3AD2024base.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class EspectaculoNumero {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "espectaculo_id")
	private Espectaculo espectaculo;

	@ManyToOne
	@JoinColumn(name = "numero_id")
	private Numero numero;

	private int orden;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Espectaculo getEspectaculo() {
		return espectaculo;
	}

	public void setEspectaculo(Espectaculo espectaculo) {
		this.espectaculo = espectaculo;
	}

	public Numero getNumero() {
		return numero;
	}

	public void setNumero(Numero numero) {
		this.numero = numero;
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

}
