package com.simao.tarea3AD2024base.modelo;

public class LogOperacion {

	private Long id;
	private String fechaHora;
	private String user;
	private String tipoOperacion;
	private String resumen;

	public LogOperacion() {
	}

	public LogOperacion(Long id, String fechaHora, String user, String tipoOperacion, String resumen) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.user = user;
		this.tipoOperacion = tipoOperacion;
		this.resumen = resumen;
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(String tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

}
