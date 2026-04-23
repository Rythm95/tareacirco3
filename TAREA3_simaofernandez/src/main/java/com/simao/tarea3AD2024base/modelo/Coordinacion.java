package com.simao.tarea3AD2024base.modelo;

import java.time.LocalDate;

import jakarta.persistence.Entity;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Entity
public class Coordinacion extends Persona {

	private boolean senior;

	private LocalDate fechaSenior;


	public boolean isSenior() {
		return senior;
	}

	public void setSenior(boolean senior) {
		this.senior = senior;
	}

	public LocalDate getFechaSenior() {
		return fechaSenior;
	}

	public void setFechaSenior(LocalDate fechaSenior) {
		this.fechaSenior = fechaSenior;
	}

}
