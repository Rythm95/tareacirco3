package com.simao.tarea3AD2024base.objectdb.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ResolucionIncidencia {

	@Id
	@GeneratedValue
	private Long id;

	private String fechaHoraResolucion;

	@Column(length = 1000)
	private String accionesRealizadas;

	private Long idPersonaResuelve;

	@ManyToOne
	private Incidencia incidencia;

	public ResolucionIncidencia() {
	}

	public ResolucionIncidencia(Long id, String fechaHoraResolucion, String accionesRealizadas, Long idPersonaResuelve,
			Incidencia incidencia) {
		super();
		this.id = id;
		this.fechaHoraResolucion = fechaHoraResolucion;
		this.accionesRealizadas = accionesRealizadas;
		this.idPersonaResuelve = idPersonaResuelve;
		this.incidencia = incidencia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFechaHoraResolucion() {
		return fechaHoraResolucion;
	}

	public void setFechaHoraResolucion(String fechaHoraResolucion) {
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

	public Incidencia getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}

}
