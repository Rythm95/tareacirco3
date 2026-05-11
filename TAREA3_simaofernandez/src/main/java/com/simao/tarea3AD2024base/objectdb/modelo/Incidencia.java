package com.simao.tarea3AD2024base.objectdb.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Incidencia {

	@Id
	@GeneratedValue
	private Long id;

	private String fechaHora;

	@Enumerated(EnumType.STRING)
	private TipoIncidencia tipo;

	@Column(length = 1000)
	private String descripcion;

	private boolean resuelta = false;

	private Long idPersonaReporta;

	private Long idEspectaculo;

	private Long idNumero;

	public Incidencia() {
	}

	public Incidencia(Long id, String fechaHora, TipoIncidencia tipo, String descripcion, boolean resuelta,
			Long idPersonaReporta, Long idEspectaculo, Long idNumero) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.resuelta = resuelta;
		this.idPersonaReporta = idPersonaReporta;
		this.idEspectaculo = idEspectaculo;
		this.idNumero = idNumero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public TipoIncidencia getTipo() {
		return tipo;
	}

	public void setTipo(TipoIncidencia tipo) {
		this.tipo = tipo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isResuelta() {
		return resuelta;
	}

	public void setResuelta(boolean resuelta) {
		this.resuelta = resuelta;
	}

	public Long getIdPersonaReporta() {
		return idPersonaReporta;
	}

	public void setIdPersonaReporta(Long idPersonaReporta) {
		this.idPersonaReporta = idPersonaReporta;
	}

	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public Long getIdNumero() {
		return idNumero;
	}

	public void setIdNumero(Long idNumero) {
		this.idNumero = idNumero;
	}

}
