package com.simao.tarea3AD2024base.modelo.mongo;

import java.util.ArrayList;
import java.util.List;

public class Trayectoria {

	private Long idEspectaculo;

	private String nombreEspectaculo;

	private List<NumeroTrayectoria> numeros = new ArrayList<>();

	public Trayectoria() {
	}

	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public String getNombreEspectaculo() {
		return nombreEspectaculo;
	}

	public void setNombreEspectaculo(String nombreEspectaculo) {
		this.nombreEspectaculo = nombreEspectaculo;
	}

	public List<NumeroTrayectoria> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<NumeroTrayectoria> numeros) {
		this.numeros = numeros;
	}

}
