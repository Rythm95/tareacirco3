package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.modelo.Especialidad;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.services.AccesoPaises;
import com.simao.tarea3AD2024base.services.CredsService;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

@Controller
public class GestionPersonasController implements Initializable {

	private PseudoClass EMPTY = PseudoClass.getPseudoClass("error");

	@FXML
	private VBox container;

	@FXML
	private ScrollPane formularioBox;
	
	@FXML
	private VBox formularioContent;
	
	@FXML
	private ScrollPane listaBox;

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

	private ToggleGroup rdGroup = new ToggleGroup();

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

	@FXML
	private Button btnForm;

	@FXML
	private Button save;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private PersonaService peService;

	@Autowired
	private CredsService crService;
	
	@Autowired
	private ApplicationEventPublisher evPublisher;

	private String getNombre() {
		return txtNombre.getText();
	}

	private String getEmail() {
		return txtEmail.getText();
	}

	private String getNacionalidad() {
		return cbNacionalidad.getValue();
	}

	private Toggle getTipo() {
		return rdGroup.getSelectedToggle();
	}

	private boolean isSenior() {
		return checkSenior.isSelected();
	}

	private LocalDate getFechaSenior() {
		return dpSenior.getValue();
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Map<String, String> nacionalidades = AccesoPaises.loadPaises();

		rbCoord.setToggleGroup(rdGroup);
		rbArt.setToggleGroup(rdGroup);

		rdGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> personaTipoForm(newVal));

		checkSenior.selectedProperty().addListener((observable, oldVal, newVal) -> showSenior(newVal));

		checkApodo.selectedProperty().addListener((observable, oldVal, newVal) -> showApodo(newVal));
		
		formularioBox.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> ajustarLayout());
		formularioContent.heightProperty().addListener((obs, oldVal, newVal) -> ajustarLayout());

		if (nacionalidades.isEmpty()) {
			save.setDisable(true);
			lblError.setText("Error al cargar las nacionalidades.");
			lblError.setVisible(true);
		} else {
			for (Map.Entry<String, String> na : nacionalidades.entrySet()) {
				cbNacionalidad.getItems().add(na.getValue());
			}
		}

		for (Especialidad esp : Especialidad.values()) {
			CheckBox cb = new CheckBox(esp.name());
			checkEspecialidades.put(esp, cb);
			especialidadesContainer.getChildren().add(cb);
		}

		cargarPersonas();

	}
	
	private void ajustarLayout() {
		if (formularioContent.getHeight() <= formularioBox.getViewportBounds().getHeight()) {
			VBox.setVgrow(formularioBox, Priority.NEVER);
		} else {
			VBox.setVgrow(formularioBox, Priority.ALWAYS);
		}
	}

	private void cargarPersonas() {

		List<Persona> listaPersonas = peService.findAll();

		container.getChildren().clear();

		if (listaPersonas.isEmpty()) {
			Label vacio = new Label("No hay personas registradas.");
			container.getChildren().add(vacio);
		} else {
			for (Persona p : listaPersonas) {
				VBox card = personaCard(p);
				container.getChildren().add(card);
			}
		}

	}

	private VBox personaCard(Persona p) {

		String subText = "";
		if (p instanceof Artista a) {
			subText = "Artista";

			if (!a.getApodo().isEmpty())
				subText = subText.concat(" - " + a.getApodo());
		}

		if (p instanceof Coordinacion c) {
			subText = "Coordinador/a";

			if (c.isSenior())
				subText = subText.concat(" Senior");
		}

		Label nombre = new Label("[id " + p.getId() + "] - " + p.getNombre());
		nombre.getStyleClass().add("card-titulo");

		Label subtitulo = new Label(subText);
		subtitulo.getStyleClass().add("card-subtitulo");

		VBox card = new VBox(nombre, subtitulo);
		card.getStyleClass().add("card");

		return card;
	}

	@FXML
	private void showForm() {
		if (formularioBox.isVisible()) {
			formularioBox.setVisible(false);
			formularioBox.setManaged(false);
			listaBox.setMaxHeight(Double.MAX_VALUE);
			btnForm.setText("Nueva Persona");
		} else {
			formularioBox.setVisible(true);
			formularioBox.setManaged(true);
			listaBox.setMaxHeight(70);
			listaBox.setMinHeight(70);
			btnForm.setText("Ocultar Formulario");
		}
	}

	@FXML
	private void limpiarForm() {
		txtNombre.clear();
		txtEmail.clear();
		txtApodo.clear();
		txtUser.clear();
		txtPass.clear();
		cbNacionalidad.getSelectionModel().clearSelection();
		cbNacionalidad.setValue(null);
		dpSenior.setValue(null);
		checkApodo.setSelected(false);
		checkEspecialidades.values().forEach(check -> check.setSelected(false));
		checkSenior.setSelected(false);
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

	@FXML
	private void save() {
		if (validarForm()) {
			return;
		}

		Persona persona;
		Credenciales creds = new Credenciales();

		if (getTipo() == rbCoord) {
			Coordinacion cord = new Coordinacion();
			cord.setSenior(isSenior());
			cord.setFechaSenior(getFechaSenior());
			persona = cord;
		} else {
			Artista art = new Artista();
			art.setApodo(getApodo());
			art.setEspecialidades(getEspecialidades());
			persona = art;
		}

		persona.setNombre(getNombre());
		persona.setEmail(getEmail());
		persona.setNacionalidad(getNacionalidad());

		creds.setUsername(getUsername());
		creds.setPassword(getPassword());
		creds.setPerfil(getTipo() == rbCoord ? Perfil.COORDINACION : Perfil.ARTISTA);

		creds.setPersona(persona);
		persona.setCredenciales(creds);

		peService.save(persona);
		
		limpiarForm();
		cargarPersonas();
		
		evPublisher.publishEvent(new NewPersonaEvent(persona));

	}

	private boolean validarForm() {

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
			} else if (peService.findByEmail(getEmail()) != null) {
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

		boolean fechaSenior = false;
		if (isSenior()) {
			fechaSenior = getFechaSenior() == null;
			dpSenior.pseudoClassStateChanged(EMPTY, fechaSenior);
		}

		boolean apodo = false;
		if (hasApodo()) {
			apodo = getApodo().isEmpty();
			txtApodo.pseudoClassStateChanged(EMPTY, apodo);
		}

		boolean especialidades = false;
		if (getTipo() == rbArt) {
			especialidades = getEspecialidades().isEmpty();
			lblErrorEspecialidades.setText("Debe seleccionar al menos una especialidad para registrar un artista.");
		}
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
			} else if (crService.findByUsername(getUsername()) != null) {
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

		return nombre || email || nacionalidad || fechaSenior || apodo || especialidades || username || password;
	}
}
