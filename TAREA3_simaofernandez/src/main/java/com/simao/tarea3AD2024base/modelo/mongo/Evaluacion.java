package com.simao.tarea3AD2024base.modelo.mongo;

public class Evaluacion {

    private String fecha;

    private Evaluador realizadaPor;

    private String comentario;

    private String nivel;

    public Evaluacion() {
    }

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public Evaluador getRealizadaPor() {
		return realizadaPor;
	}

	public void setRealizadaPor(Evaluador realizadaPor) {
		this.realizadaPor = realizadaPor;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

    
}
