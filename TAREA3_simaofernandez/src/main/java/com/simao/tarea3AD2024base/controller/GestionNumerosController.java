package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.services.NumeroService;
import com.simao.tarea3AD2024base.services.PersonaService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@Controller
public class GestionNumerosController implements Initializable {

	@Autowired
	private PersonaService peService;

	@Autowired
	private NumeroService nuService;

	@FXML
	private VBox container;

	@FXML
	private Button btnForm;

	@FXML
	private ScrollPane formularioBox;

	@FXML
	private TextField txtNombre;
	
	@FXML
	private Spinner<Integer> spMinutos;
	
	@FXML
	private ComboBox<String> cbDecimal;

	@FXML
	private ListView<Persona> lvArtistas;
	
	@FXML
	private ComboBox<String> cbArtista;

	@FXML
	private Label lblError;

	@FXML
	private Button save;
	
	private String getNombre() {
		return txtNombre.getText();
	}
	
	private double getDuracion() {
		int min = spMinutos.getValue();
		if (cbDecimal.getValue() == ".5")
			min += 0.5;
		return min;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		List<Persona> listaArtistas = peService.findByPerfil(Perfil.ARTISTA);

		List<Numero> listaNumeros = nuService.findAll();

		spMinutos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
		
		cbDecimal.getItems().addAll(".0", ".5");
		
		lvArtistas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
				
		if (listaArtistas.isEmpty()) {
			save.setDisable(true);
			lblError.setText("Registra un artista antes de crear un número.");
			lblError.setVisible(true);
		} else {
			lvArtistas.getItems().addAll(listaArtistas);
			for (Persona p : listaArtistas) {
				cbArtista.getItems().add(p.getNombre());
			}
		}

		if (listaNumeros.isEmpty()) {
			Label vacio = new Label("No hay números registrados.");
			container.getChildren().add(vacio);
		} else {
			for (Numero n : listaNumeros) {
				VBox card = numeroCard(n);
				container.getChildren().add(card);
			}
		}

	}

	private VBox numeroCard(Numero n) {

		Label nombre = new Label("[id " + n.getId() + "] - " + n.getNombre());
		nombre.getStyleClass().add("card-titulo");

		VBox card = new VBox(nombre);
		card.getStyleClass().add("card");

		return card;
	}

	@FXML
	private void showForm() {

		if (formularioBox.isVisible()) {
			formularioBox.setVisible(false);
			formularioBox.setManaged(false);
			btnForm.setText("Nuevo Número");
		} else {
			formularioBox.setVisible(true);
			formularioBox.setManaged(true);
			btnForm.setText("Ocultar Formulario");
		}

	}

	@FXML
	public void limpiarForm() {
		txtNombre.clear();

	}

}