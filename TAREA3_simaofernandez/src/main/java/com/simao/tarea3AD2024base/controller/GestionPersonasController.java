package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.services.PersonaService;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

@Controller
public class GestionPersonasController implements Initializable {

	@FXML
	private VBox container;

	@FXML
	private ScrollPane formularioBox;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtEmail;

	@FXML
	private ComboBox<String> cbNacionalidad;

	@FXML
	private RadioButton rbCoord;

	@FXML
	private RadioButton rbArt;

	ToggleGroup rdGroup = new ToggleGroup();

	@FXML
	private VBox coordContainer;

	@FXML
	private CheckBox checkSenior;

	@FXML
	private DatePicker dpSenior;

	@FXML
	private VBox artContainer;

	@FXML
	private CheckBox checkApodo;

	@FXML
	private TextField txtApodo;

	@FXML
	private VBox especialidadesContainer;

	@FXML
	private TextField txtUser;

	@FXML
	private TextField txtPass;

	@FXML
	private Label lblError;

	@FXML
	private Button btnForm;

	@FXML
	private Button save;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private PersonaService peService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		List<Persona> listaPersonas = peService.findAll();

		rbCoord.setToggleGroup(rdGroup);
		rbArt.setToggleGroup(rdGroup);

		rdGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> personaTipoForm(newVal));

		if (listaPersonas.isEmpty()) {
			Label vacio = new Label("No hay personas registradas.");
			container.getChildren().add(vacio);
		}

		for (Persona p : listaPersonas) {
			VBox card = personaCard(p);
			container.getChildren().add(card);
		}

	}

	private VBox personaCard(Persona p) {

		Label nombre = new Label("[id " + p.getId() + "] - " + p.getNombre());
		nombre.getStyleClass().add("titulo-card");

		VBox card = new VBox(nombre);
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
	}

	@FXML
	public void personaTipoForm(Toggle toggle) {
		coordContainer.setVisible(true);
		coordContainer.setManaged(true);

		artContainer.setVisible(false);
		artContainer.setManaged(false);
	}

	@FXML
	public void senior() {
		if (checkSenior.isSelected()) {
			dpSenior.setVisible(false);
			dpSenior.setManaged(false);
		} else {
			dpSenior.setVisible(true);
			dpSenior.setManaged(true);
		}
	}

	@FXML
	public void save() {
		txtNombre.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), getNombre().isEmpty());
	}

	public String getNombre() {
		return txtNombre.getText();
	}
}
