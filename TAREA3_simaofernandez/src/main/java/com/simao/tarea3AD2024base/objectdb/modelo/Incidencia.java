package com.simao.tarea3AD2024base.objectdb.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Incidencia {

	@Id
	@GeneratedValue
	private Long id;

	private LocalDateTime fechaHora;

	@Enumerated(EnumType.STRING)
	private TipoIncidencia tipo;

	@Column(length = 1000)
	private String descripcion;

	private boolean resuelta = false;

	private Long idPersonaReporta;

	private Long idEspectaculo;

	private Long idNumero;

	@OneToOne(mappedBy = "incidencia", cascade = CascadeType.ALL)
	private ResolucionIncidencia resolucion;

	public Incidencia() {
	}

	public Incidencia(LocalDateTime fechaHora, TipoIncidencia tipo, String descripcion, Long idPersonaReporta,
			Long idEspectaculo, Long idNumero) {
		super();
		this.fechaHora = fechaHora;
		this.tipo = tipo;
		this.descripcion = descripcion;
		this.resuelta = false;
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

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
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

	public ResolucionIncidencia getResolucion() {
		return resolucion;
	}

	public void setResolucion(ResolucionIncidencia resolucion) {
		this.resolucion = resolucion;
	}

}
