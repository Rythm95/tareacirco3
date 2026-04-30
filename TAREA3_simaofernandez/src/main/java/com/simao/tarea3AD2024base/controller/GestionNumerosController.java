package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.services.NumeroService;
import com.simao.tarea3AD2024base.services.PersonaService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

@Controller
public class GestionNumerosController implements Initializable {

	private PseudoClass EMPTY = PseudoClass.getPseudoClass("error");

	@Lazy
	@Autowired
	private StageManager stageManager;
	
	@Autowired
	private PersonaService peService;

	@Autowired
	private NumeroService nuService;

	@Autowired
	private ApplicationEventPublisher evPublisher;
	
	@Autowired
	private Session session;

	@EventListener
	public void onNewPersona(NewPersonaEvent event) {
		if (event.getPersona() instanceof Artista) {
			cargarArtistas();
		}
	}

	@FXML
	private VBox container;

	@FXML
	private Button btnForm;

	@FXML
	private ScrollPane formularioBox;

	@FXML
	private VBox formularioContent;

	@FXML
	private TextField txtNombre;

	@FXML
	private Spinner<Integer> spMinutos;

	@FXML
	private ComboBox<String> cbDecimal;

	@FXML
	private ScrollPane listaBox;

	@FXML
	private VBox containerArtistas;

	private Map<Artista, CheckBox> checkArtistas = new HashMap<>();

	@FXML
	private Label lblError;

	@FXML
	private Label lblErrorNombre;

	@FXML
	private Button save;

	private String getNombre() {
		return txtNombre.getText();
	}

	private double getDuracion() {
		double min = spMinutos.getValue();
		if (cbDecimal.getValue().equals(".5"))
			min += 0.5;
		return min;
	}

	private List<Artista> getArtistas() {
		return checkArtistas.entrySet().stream().filter(es -> es.getValue().isSelected()).map(Map.Entry::getKey)
				.toList();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		spMinutos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
		cbDecimal.getItems().addAll(".0", ".5");
		cbDecimal.getSelectionModel().selectFirst();

		cargarArtistas();
		cargarNumeros();

		formularioBox.viewportBoundsProperty().addListener((obs, oldVal, newVal) -> ajustarLayout());
		formularioContent.heightProperty().addListener((obs, oldVal, newVal) -> ajustarLayout());

	}

	private void ajustarLayout() {
		if (formularioContent.getHeight() <= formularioBox.getViewportBounds().getHeight()) {
			VBox.setVgrow(formularioBox, Priority.NEVER);
		} else {
			VBox.setVgrow(formularioBox, Priority.ALWAYS);
		}
	}

	private void cargarArtistas() {
		List<Persona> listaArtistas = peService.findByPerfil(Perfil.ARTISTA);

		containerArtistas.getChildren().clear();

		if (listaArtistas.isEmpty()) {
			save.setDisable(true);
			lblError.setText("Registra un artista antes de crear un número.");
			lblError.setVisible(true);
		} else {
			for (Persona p : listaArtistas) {
				Artista a = (Artista) p;
				CheckBox cb = new CheckBox(p.getNombre());
				checkArtistas.put(a, cb);
				containerArtistas.getChildren().add(cb);
			}
		}
	}

	private void cargarNumeros() {

		List<Numero> listaNumeros = nuService.findAll();

		container.getChildren().clear();

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

		Label subtitulo = new Label(n.getDuracion() + " Minutos.");
		subtitulo.getStyleClass().add("card-subtitulo");

		VBox card = new VBox(nombre, subtitulo);
		card.getStyleClass().add("card");

		card.setOnMouseClicked(event -> {
			session.setNumeroId(n.getId());
			openNumero();
		});
		
		return card;
	}
	
	private void openNumero() {
		stageManager.switchScene(FxmlView.NUMERO);
	}

	@FXML
	private void showForm() {

		if (formularioBox.isVisible()) {
			formularioBox.setVisible(false);
			formularioBox.setManaged(false);
			listaBox.setMaxHeight(Double.MAX_VALUE);
			btnForm.setText("Nuevo Número");
		} else {
			formularioBox.setVisible(true);
			formularioBox.setManaged(true);
			listaBox.setMaxHeight(70);
			listaBox.setMinHeight(70);
			btnForm.setText("Ocultar Formulario");
		}

	}

	@FXML
	private void save() {
		if (validarForm()) {
			return;
		}

		Numero num = new Numero();
		num.setNombre(getNombre());
		num.setDuracion(getDuracion());
		num.setArtistas(getArtistas());

		nuService.save(num);

		limpiarForm();
		cargarNumeros();

		evPublisher.publishEvent(new NewNumeroEvent(num));
	}

	private boolean validarForm() {

		boolean nombre = getNombre().isEmpty();
		txtNombre.pseudoClassStateChanged(EMPTY, nombre);
		if (nuService.findByNombre(getNombre()) != null) {
			nombre = true;
			lblErrorNombre.setText("Ya existe un número con ese nombre.");

			lblErrorNombre.setManaged(nombre);
			lblErrorNombre.setVisible(nombre);
		} else {
			lblErrorNombre.setManaged(false);
			lblErrorNombre.setVisible(false);
		}

		boolean artistas = getArtistas().isEmpty();
		lblError.setText("Debe seleccionar al menos un artista que participará en el número.");

		lblError.setManaged(artistas);
		lblError.setVisible(artistas);

		return nombre || artistas;
	}

	@FXML
	public void limpiarForm() {
		txtNombre.clear();
		spMinutos.getValueFactory().setValue(1);
		cbDecimal.setValue(".0");
		checkArtistas.values().forEach(check -> check.setSelected(false));

	}

}