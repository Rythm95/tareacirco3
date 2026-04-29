/**
* Clase Session.java
*
* @author Simao Fernandez Gervasoni
* @version 1.0
*/
package com.simao.tarea3AD2024base.modelo;

import org.springframework.stereotype.Component;

@Component
public class Session {
	private Long id;
	private String nombre;
	private Perfil perfil = null;
	private Long idEspectaculo = null;
	private Long idNumero = null;
	private Long idPersona = null;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Long getEspectaculo() {
		return idEspectaculo;
	}

	public void setEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public Long getNumero() {
		return idNumero;
	}

	public void setNumero(Long idNumero) {
		this.idNumero = idNumero;
	}

	public Long getPersona() {
		return idPersona;
	}

	public void setPersona(Long idPersona) {
		this.idPersona = idPersona;
	}
}
