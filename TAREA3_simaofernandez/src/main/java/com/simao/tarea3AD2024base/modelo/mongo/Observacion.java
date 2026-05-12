package com.simao.tarea3AD2024base.modelo.mongo;

public class Observacion {

	private String fecha;

	private String texto;

	private String autor;

	public Observacion() {
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}
}