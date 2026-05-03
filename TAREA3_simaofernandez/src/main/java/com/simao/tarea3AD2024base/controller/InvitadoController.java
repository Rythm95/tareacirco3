package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.services.EspectaculoService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

@Controller
public class InvitadoController implements Initializable {

	@FXML
	private VBox espectaculosContainer;
	
	@FXML
	private Button btnInicioSesion;
	
	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private EspectaculoService services;	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		List<Espectaculo> listaEspectaculos = services.findAll();
		

		if (listaEspectaculos.isEmpty()) {
	        Label vacio = new Label("No hay espectáculos programados.");
	        espectaculosContainer.getChildren().add(vacio);
	        return;
	    }

	    for (Espectaculo es : listaEspectaculos) {
	        VBox card = espectaculoCard(es);
	        espectaculosContainer.getChildren().add(card);
	    }

	}
	
	private VBox espectaculoCard(Espectaculo e) {

	    Label nombre = new Label(e.getNombre());
	    nombre.getStyleClass().add("card-titulo");

	    Label fechas = new Label(
	        "Del " + e.getFechaini() + " al " + e.getFechafin()
	    );
	    fechas.getStyleClass().add("card-subtitulo");

	    VBox caja = new VBox(nombre, fechas);
	    caja.getStyleClass().add("card-invitado");

	    return caja;
	}

	public void login() {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	public void exit(ActionEvent event) {
		System.exit(0);
	}
}
