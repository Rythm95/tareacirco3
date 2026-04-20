package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.services.EspectaculoService;
import com.simao.tarea3AD2024base.services.NumeroService;
import com.simao.tarea3AD2024base.services.PersonaService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@Controller
public class GestionEspectaculosController implements Initializable {

	@FXML
	private VBox container;

	@FXML
	private VBox formularioBox;
	
	@FXML
	private TextField txtNombre;
	
	@FXML
	private DatePicker dpInicio;
	
	@FXML
	private DatePicker dpFin;
	
	@FXML
	private Label lblError;

	@FXML
	private Button btnForm;
	
	@FXML
	private Button save;

	@FXML
	private ComboBox<String> cbCoordinador;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private EspectaculoService esService;

	@Autowired
	private NumeroService nuService;

	@Autowired
	private PersonaService peService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		List<Espectaculo> listaEspectaculos = esService.findAll();
		List<Numero> listaNumeros = nuService.findAll();

		List<Persona> listaCoordinacion = peService.findByPerfil(Perfil.COORDINACION);

		if (listaCoordinacion.isEmpty()) {
			save.setDisable(true);
			lblError.setText("Registra un coordinador antes de crear un espectáculo.");
			lblError.setVisible(true);
		}
		
		for (Persona p : listaCoordinacion) {
			cbCoordinador.getItems().add(p.getNombre());	
		}

		if (listaEspectaculos.isEmpty()) {
			Label vacio = new Label("No hay espectáculos programados.");
			container.getChildren().add(vacio);
		}

		for (Espectaculo es : listaEspectaculos) {
			VBox card = espectaculoCard(es);
			container.getChildren().add(card);
		}

	}

	private VBox espectaculoCard(Espectaculo e) {

		Label nombre = new Label("[id " + e.getId() + "] - " + e.getNombre());
		nombre.getStyleClass().add("titulo-card");

		Label fechas = new Label("Del " + e.getFechaini() + " al " + e.getFechafin());
		fechas.getStyleClass().add("fecha");

		VBox card = new VBox(nombre, fechas);
		card.getStyleClass().add("card");

		return card;
	}

	@FXML
	private void showForm() {
		if (formularioBox.isVisible()) {
			formularioBox.setVisible(false);
			formularioBox.setManaged(false);
			btnForm.setText("Nuevo Espectáculo");
		} else {
			formularioBox.setVisible(true);
			formularioBox.setManaged(true);
			btnForm.setText("Ocultar Formulario");
		}
	}
	
	@FXML
	public void limpiarForm() {
		txtNombre.clear();
		dpInicio.setValue(null);
		dpFin.setValue(null);
		cbCoordinador.setValue(null);
		
	}

	public void login() {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	public void exit(ActionEvent event) {
		System.exit(0);
	}
}
