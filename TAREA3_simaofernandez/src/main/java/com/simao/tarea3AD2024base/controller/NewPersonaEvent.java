package com.simao.tarea3AD2024base.controller;

import com.simao.tarea3AD2024base.modelo.Persona;

public class NewPersonaEvent {
	private final Persona persona;
	
	public NewPersonaEvent(Persona persona) {
		this.persona = persona;
	}
	
	public Persona getPersona() {
		return persona;
	}
}
