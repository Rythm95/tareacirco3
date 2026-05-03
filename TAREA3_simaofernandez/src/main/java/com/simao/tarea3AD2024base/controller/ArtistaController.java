package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.modelo.Especialidad;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.services.AccesoPaises;
import com.simao.tarea3AD2024base.services.CredsService;
import com.simao.tarea3AD2024base.services.PersonaService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@Controller
public class ArtistaController implements Initializable {

	private PseudoClass EMPTY = PseudoClass.getPseudoClass("error");

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private Session session;

	@Autowired
	private PersonaService peService;

	@Autowired
	private CredsService crService;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtEmail;

	@FXML
	private ComboBox<String> cbNacionalidad;

	@FXML
	private CheckBox checkApodo;

	@FXML
	private TextField txtApodo;

	@FXML
	private VBox especialidadesContainer;

	private Map<Especialidad, CheckBox> checkEspecialidades = new HashMap<>();

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
	private Label lblErrorEspecialidades;

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

	private boolean hasApodo() {
		return checkApodo.isSelected();
	}

	private String getApodo() {
		return txtApodo.getText();
	}

	private List<Especialidad> getEspecialidades() {
		return checkEspecialidades.entrySet().stream().filter(es -> es.getValue().isSelected()).map(Map.Entry::getKey)
				.toList();
	}

	private String getUsername() {
		return txtUser.getText();
	}

	private String getPassword() {
		return txtPass.getText();
	}

	private Artista getArtistaInSession() {
		return (Artista) peService.findArtistaEspecialidades(session.getPersonaId());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (session.getPersonaId() == null) {
			System.out.println("Error!!!");
		} else {

			checkApodo.selectedProperty().addListener((observable, oldVal, newVal) -> showApodo(newVal));

			cargarDatos();
		}

	}

	private void showApodo(boolean select) {
		txtApodo.setVisible(select);
		txtApodo.setManaged(select);
	}

	private void cargarDatos() {

		Credenciales cred = crService.findByPersona_Id(session.getPersonaId());
		Map<String, String> nacionalidades = AccesoPaises.loadPaises();

		for (Map.Entry<String, String> na : nacionalidades.entrySet()) {
			cbNacionalidad.getItems().add(na.getValue());
		}

		Artista art = getArtistaInSession();

		txtNombre.setText(art.getNombre());
		txtEmail.setText(art.getEmail());

		cbNacionalidad.setValue(art.getNacionalidad());

		if (!art.getApodo().isEmpty()) {
			checkApodo.setSelected(true);
			txtApodo.setText(art.getApodo());
		}

		List<Especialidad> especialidades = art.getEspecialidades();
		especialidadesContainer.getChildren().clear();
		for (Especialidad esp : Especialidad.values()) {
			CheckBox cb = new CheckBox(esp.name());
			if (especialidades.contains(esp)) {
				cb.setSelected(true);
			}
			checkEspecialidades.put(esp, cb);
			especialidadesContainer.getChildren().add(cb);
		}

		txtUser.setText(cred.getUsername());
		txtPass.setText(cred.getPassword());

	}

	@FXML
	private void goBack() {
		session.setPersonaId(null);
		stageManager.switchScene(FxmlView.ADMINISTRADOR);
	}

	@FXML
	public void save() {
		if (validarForm()) {
			return;
		}

		Artista art = new Artista();
		Credenciales cr = new Credenciales();

		art.setNombre(getNombre());
		art.setEmail(getEmail());
		art.setNacionalidad(getNacionalidad());

		if (checkApodo.isSelected())
			art.setApodo(getApodo());
		else
			art.setApodo("");
		art.setEspecialidades(getEspecialidades());

		cr.setUsername(getUsername());
		cr.setPassword(getPassword());
		cr.setPerfil(Perfil.ARTISTA);

		cr.setPersona(art);
		art.setCredenciales(cr);

		peService.updateArtista(session.getPersonaId(), art, cr);

		goBack();

	}

	private boolean validarForm() {

		Persona newPersona = peService.findByEmail(getEmail());
		Credenciales newCred = crService.findByUsername(getUsername());

		boolean nombre = getNombre().isEmpty();
		txtNombre.pseudoClassStateChanged(EMPTY, nombre);
		if (!nombre) {
			if (!Pattern.matches("^[\\p{L}]+( [\\p{L}]+)*$", getNombre())) {
				nombre = true;
				lblErrorNombre.setText("El nombre solo puede contener caracteres unicode y espacios.");
			}
			lblErrorNombre.setManaged(nombre);
			lblErrorNombre.setVisible(nombre);
		} else {
			lblErrorNombre.setManaged(false);
			lblErrorNombre.setVisible(false);
		}

		boolean email = getEmail().isEmpty();
		txtEmail.pseudoClassStateChanged(EMPTY, email);
		if (!email) {
			if (!Pattern.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", getEmail())) {
				email = true;
				lblErrorEmail.setText("El email no es válido.");
			} else if (newPersona != null && !newPersona.getId().equals(session.getPersonaId())) {
				email = true;
				lblErrorEmail.setText("Este correo ya está registrado en la base de datos.");
			}

			lblErrorEmail.setManaged(email);
			lblErrorEmail.setVisible(email);
		} else {
			lblErrorEmail.setManaged(false);
			lblErrorEmail.setVisible(false);
		}

		boolean nacionalidad = getNacionalidad() == null;
		cbNacionalidad.pseudoClassStateChanged(EMPTY, nacionalidad);

		boolean apodo = false;
		if (hasApodo()) {
			apodo = getApodo() == null;
			txtApodo.pseudoClassStateChanged(EMPTY, apodo);
		}

		boolean especialidades = false;
		especialidades = getEspecialidades().isEmpty();
		lblErrorEspecialidades.setText("Debe seleccionar al menos una especialidad para registrar un artista.");
		lblErrorEspecialidades.setManaged(especialidades);
		lblErrorEspecialidades.setVisible(especialidades);

		boolean username = getUsername().isEmpty();
		txtUser.pseudoClassStateChanged(EMPTY, username);
		if (!username) {
			if (getUsername().length() <= 2) {
				username = true;
				lblErrorUser.setText("El nombre de usuario debe contener más de 2 caracteres.");
			} else if (getUsername().contains(" ")) {
				username = true;
				lblErrorUser.setText("El nombre de usuario no debe contener espacios.");
			} else if (!Pattern.matches("^[a-z]+$", getUsername())) {
				username = true;
				lblErrorUser.setText(
						"El nombre de usuario no debe contener números ni letras mayúsculas o con tíldes o dieresis.");
			} else if (newCred != null && !newCred.getId().equals(session.getPersonaId())) {
				username = true;
				lblErrorUser.setText("Este nombre de usuario ya está registrado en la base de datos.");
			}

			lblErrorUser.setManaged(username);
			lblErrorUser.setVisible(username);
		} else {
			lblErrorUser.setManaged(false);
			lblErrorUser.setVisible(false);
		}

		boolean password = getPassword().isEmpty();
		txtPass.pseudoClassStateChanged(EMPTY, password);
		if (!password) {
			if (getPassword().length() <= 2) {
				password = true;
				lblErrorPass.setText("La contraseña debe contener más de 2 caracteres.");
			} else if (getPassword().contains(" ")) {
				password = true;
				lblErrorPass.setText("La contraseña no debe contener espacios.");
			}
			lblErrorPass.setManaged(password);
			lblErrorPass.setVisible(password);
		} else {
			lblErrorPass.setManaged(false);
			lblErrorPass.setVisible(false);
		}

		return nombre || email || nacionalidad || especialidades || apodo || username || password;
	}

	@FXML
	private void reiniciarForm() {
		cargarDatos();
	}
}