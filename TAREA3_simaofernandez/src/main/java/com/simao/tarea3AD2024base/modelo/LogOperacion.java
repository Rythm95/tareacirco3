package com.simao.tarea3AD2024base.modelo;

import java.time.LocalDateTime;

public class LogOperacion {

	private Long id;
	private LocalDateTime fechaHora;
	private String typeOperacion;
	private String resumen;

	public LogOperacion() {
	}

	public LogOperacion(Long id, LocalDateTime fechaHora, String typeOperacion, String resumen) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.typeOperacion = typeOperacion;
		this.resumen = resumen;
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

	public String getTypeOperacion() {
		return typeOperacion;
	}

	public void setTypeOperacion(String typeOperacion) {
		this.typeOperacion = typeOperacion;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

}
