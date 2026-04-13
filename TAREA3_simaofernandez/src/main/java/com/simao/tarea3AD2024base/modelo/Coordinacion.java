package com.simao.tarea3AD2024base.modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Entity
@Table(name = "Coordinacion")
public class Coordinacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private long id;

	private boolean senior;

	private LocalDate fechaSenior;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

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
