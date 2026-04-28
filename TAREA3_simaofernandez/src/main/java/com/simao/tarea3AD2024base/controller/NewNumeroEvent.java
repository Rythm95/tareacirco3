package com.simao.tarea3AD2024base.controller;

import com.simao.tarea3AD2024base.modelo.Numero;

public class NewNumeroEvent {
	private final Numero numero;
	
	public NewNumeroEvent(Numero numero) {
		this.numero = numero;
	}
	
	public Numero getNumero() {
		return numero;
	}
}
