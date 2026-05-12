package com.simao.tarea3AD2024base.objectdb.modelo;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class ResolucionIncidencia {

	@Id
	@GeneratedValue
	private Long id;

	private LocalDateTime fechaHoraResolucion;

	@Column(length = 1000)
	private String accionesRealizadas;

	private Long idPersonaResuelve;

	private Long idIncidencia;

	public ResolucionIncidencia() {
	}

	public ResolucionIncidencia(LocalDateTime fechaHoraResolucion, String accionesRealizadas, Long idPersonaResuelve,
			Long idIncidencia) {
		super();
		this.fechaHoraResolucion = fechaHoraResolucion;
		this.accionesRealizadas = accionesRealizadas;
		this.idPersonaResuelve = idPersonaResuelve;
		this.idIncidencia = idIncidencia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaHoraResolucion() {
		return fechaHoraResolucion;
	}

	public void setFechaHoraResolucion(LocalDateTime fechaHoraResolucion) {
		this.fechaHoraResolucion = fechaHoraResolucion;
	}

	public String getAccionesRealizadas() {
		return accionesRealizadas;
	}

	public void setAccionesRealizadas(String accionesRealizadas) {
		this.accionesRealizadas = accionesRealizadas;
	}

	public Long getIdPersonaResuelve() {
		return idPersonaResuelve;
	}

	public void setIdPersonaResuelve(Long idPersonaResuelve) {
		this.idPersonaResuelve = idPersonaResuelve;
	}

	public Long getIncidencia() {
		return idIncidencia;
	}

	public void setIncidencia(Long idIncidencia) {
		this.idIncidencia = idIncidencia;
	}

}
