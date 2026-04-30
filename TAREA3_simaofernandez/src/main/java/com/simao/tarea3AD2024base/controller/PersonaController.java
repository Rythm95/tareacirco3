package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.modelo.Especialidad;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.services.AccesoPaises;
import com.simao.tarea3AD2024base.services.NumeroService;
import com.simao.tarea3AD2024base.services.PersonaService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

@Controller
public class PersonaController implements Initializable {

	private PseudoClass EMPTY = PseudoClass.getPseudoClass("error");

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private Session session;

	@Autowired
	private PersonaService peService;

	@Autowired
	private NumeroService nuService;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtEmail;

	@FXML
	private ComboBox<String> cbNacionalidad;

	@FXML
	private VBox coordContainer;

	@FXML
	private CheckBox checkSenior;

	@FXML
	private DatePicker dpSenior;

	@FXML
	private TextField txtUser;

	@FXML
	private TextField txtPass;

	@FXML
	private Label lblError;

	@FXML
	private Label lblErrorNombre;

	@FXML
	private Label lblErrorEmail;

	@FXML
	private Label lblErrorUser;

	@FXML
	private Label lblErrorPass;

	private String getNombre() {
		return txtNombre.getText();
	}

	private String getEmail() {
		return txtEmail.getText();
	}

	private String getNacionalidad() {
		return cbNacionalidad.getValue();
	}

	private boolean isSenior() {
		return checkSenior.isSelected();
	}

	private LocalDate getFechaSenior() {
		return dpSenior.getValue();
	}

	private String getUsername() {
		return txtUser.getText();
	}

	private String getPassword() {
		return txtPass.getText();
	}

	private Coordinacion getCoordinadorInSession() {
		return (Coordinacion) peService.find(session.getPersonaId());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (session.getPersonaId() == null) {
			System.out.println("Error!!!");
		} else {
			rbCoord.setToggleGroup(rdGroup);
			rbArt.setToggleGroup(rdGroup);

			rdGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> personaTipoForm(newVal));

			checkSenior.selectedProperty().addListener((observable, oldVal, newVal) -> showSenior(newVal));

			checkApodo.selectedProperty().addListener((observable, oldVal, newVal) -> showApodo(newVal));

			cargarDatos();
		}

	}

	private void personaTipoForm(Toggle toggle) {
		boolean isCoord = (toggle == rbCoord);

		coordContainer.setVisible(isCoord);
		coordContainer.setManaged(isCoord);

		artContainer.setVisible(!isCoord);
		artContainer.setManaged(!isCoord);
	}

	private void showSenior(boolean select) {
		dpSenior.setVisible(select);
		dpSenior.setManaged(select);
	}

	private void showApodo(boolean select) {
		txtApodo.setVisible(select);
		txtApodo.setManaged(select);
	}

	private void cargarDatos() {

		Credenciales cred = getPersonaInSession().getCredenciales();
		Map<String, String> nacionalidades = AccesoPaises.loadPaises();

		for (Map.Entry<String, String> na : nacionalidades.entrySet()) {
			cbNacionalidad.getItems().add(na.getValue());
		}

		for (Especialidad esp : Especialidad.values()) {
			CheckBox cb = new CheckBox(esp.name());
			checkEspecialidades.put(esp, cb);
			especialidadesContainer.getChildren().add(cb);
		}

		if (cred.getPerfil() == Perfil.COORDINACION) {
			rdGroup.selectToggle(rbCoord);
			Coordinacion cord = (Coordinacion) getPersonaInSession();

			checkSenior.setSelected(cord.isSenior());
			if (cord.isSenior())
				dpSenior.setValue(cord.getFechaSenior());

		} else {
			rdGroup.selectToggle(rbArt);
			Artista art = (Artista) getPersonaInSession();

			checkApodo.setSelected(art.getApodo() != null);
			if (art.getApodo() != null)
				txtApodo.setText(art.getApodo());

		}

	}

	@FXML
	private void goBack() {
		Perfil p = session.getPerfil();

		if (p != null) {
			switch (p) {
			case ARTISTA:
				stageManager.switchScene(FxmlView.ARTISTA);
				break;
			case COORDINACION:
				stageManager.switchScene(FxmlView.COORDINADOR);
				break;
			}
		} else {
			stageManager.switchScene(FxmlView.ADMINISTRADOR);
		}

	}

	@FXML
	public void save() {
		if (validarForm()) {
			return;
		}
		Numero nu = new Numero();

		nu.setNombre(getNombre());

		//nuService.update(session.getNumeroId(), nu);

		goBack();

	}

	private boolean validarForm() {

		boolean nombre = getNombre().isEmpty();
		txtNombre.pseudoClassStateChanged(EMPTY, nombre);
		Numero newNumero = nuService.findByNombre(getNombre());

		if (newNumero != null && !newNumero.getId().equals(session.getNumeroId())) {
			nombre = true;
			lblErrorNombre.setText("Ya existe un número con ese nombre.");

			lblErrorNombre.setManaged(nombre);
			lblErrorNombre.setVisible(nombre);
		} else {
			lblErrorNombre.setManaged(false);
			lblErrorNombre.setVisible(false);
		}

		return nombre;
	}

	@FXML
	private void reiniciarForm() {
		cargarDatos();
	}
}