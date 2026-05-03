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
	private Perfil perfil = null;
	private Long idEspectaculo = null;
	private Long idNumero = null;
	private Long idPersona = null;

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public Long getEspectaculoId() {
		return idEspectaculo;
	}

	public void setEspectaculoId(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public Long getNumeroId() {
		return idNumero;
	}

	public void setNumeroId(Long idNumero) {
		this.idNumero = idNumero;
	}

	public Long getPersonaId() {
		return idPersona;
	}

	public void setPersonaId(Long idPersona) {
		this.idPersona = idPersona;
	}
}
