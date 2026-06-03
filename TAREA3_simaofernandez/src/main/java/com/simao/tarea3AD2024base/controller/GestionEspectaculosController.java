package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.services.DossierArtisticoService;
import com.simao.tarea3AD2024base.services.EspectaculoService;
import com.simao.tarea3AD2024base.services.NumeroService;
import com.simao.tarea3AD2024base.services.PersonaService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

@Controller
public class GestionEspectaculosController implements Initializable {

	private PseudoClass EMPTY = PseudoClass.getPseudoClass("error");

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private EspectaculoService esService;

	@Autowired
	private NumeroService nuService;

	@Autowired
	private PersonaService peService;
	
	@Autowired
	private DossierArtisticoService daService;

	@Autowired
	private Session session;

	@EventListener
	public void onNewPersona(NewPersonaEvent event) {
		if (event.getPersona() instanceof Coordinacion)
			cargarCoordinadores();
		else
			cargarArtistas();
	}

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
	private TextField txtNombreN;

	@FXML
	private DatePicker dpInicio;

	@FXML
	private DatePicker dpFin;

	@FXML
	private ListView<Numero> lvNumeros;

	private ObservableList<Numero> numSelected = FXCollections.observableArrayList();

	@FXML
	private Label lblError;

	@FXML
	private Label lblErrorN;

	@FXML
	private Label lblErrorNombre;

	@FXML
	private Label lblErrorNombreN;

	@FXML
	private Spinner<Integer> spMinutos;

	@FXML
	private ComboBox<String> cbDecimal;

	private Map<Artista, CheckBox> checkArtistas = new HashMap<>();

	@FXML
	private VBox containerArtistas;

	@FXML
	private Label lblErrorFecha;

	@FXML
	private Label lblErrorNumeros;

	@FXML
	private Button btnForm;

	@FXML
	private Button save;

	@FXML
	private ComboBox<String> cbCoordinador;

	private String getNombre() {
		return txtNombre.getText();
	}

	private String getNombreN() {
		return txtNombreN.getText();
	}

	private LocalDate getFechaIni() {
		return dpInicio.getValue();
	}

	private LocalDate getFechaFin() {
		return dpFin.getValue();
	}

	private Coordinacion getCoordinador() {
		if (session.getPerfil() == Perfil.COORDINACION)
			return (Coordinacion) peService.find(session.getUserId());
		else
			return (Coordinacion) peService.findByNombre(cbCoordinador.getValue());
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
		cargarNumeros();
		cargarEspectaculos();
		cargarArtistas();

		spMinutos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
		cbDecimal.getItems().addAll(".0", ".5");
		cbDecimal.getSelectionModel().selectFirst();

		if (session.getPerfil() == Perfil.COORDINACION) {
			cbCoordinador.setVisible(false);
			cbCoordinador.setManaged(false);
		} else {
			cargarCoordinadores();
		}

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

	private void cargarCoordinadores() {
		List<Persona> listaCoordinacion = peService.findByPerfil(Perfil.COORDINACION);

		if (listaCoordinacion.isEmpty()) {
			save.setDisable(true);
			lblError.setText("Registra un coordinador antes de crear un espectáculo.");
			lblError.setVisible(true);
		} else {
			save.setDisable(false);
			lblError.setVisible(false);
			cbCoordinador.getItems().clear();
			for (Persona p : listaCoordinacion) {
				cbCoordinador.getItems().add(p.getNombre());
			}
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
			save.setDisable(false);
			lblError.setVisible(false);
			for (Persona p : listaArtistas) {
				Artista a = (Artista) p;
				CheckBox cb = new CheckBox(p.getNombre());
				checkArtistas.put(a, cb);
				containerArtistas.getChildren().add(cb);
			}
		}
	}

	private void cargarNumeros() {
		lblErrorNumeros.setVisible(false);
		lblErrorNumeros.setManaged(false);
		lvNumeros.setItems(numSelected);
	}

	private void cargarEspectaculos() {
		List<Espectaculo> listaEspectaculos = esService.findAll();

		if (listaEspectaculos.isEmpty()) {
			Label vacio = new Label("No hay espectáculos programados.");
			container.getChildren().add(vacio);
		} else {
			container.getChildren().clear();
			for (Espectaculo es : listaEspectaculos) {
				VBox card = espectaculoCard(es);
				container.getChildren().add(card);
			}
		}
	}

	private VBox espectaculoCard(Espectaculo e) {

		Label nombre = new Label("[id " + e.getId() + "] - " + e.getNombre());
		nombre.getStyleClass().add("card-titulo");

		Label fechas = new Label("Del " + e.getFechaini() + " al " + e.getFechafin());
		fechas.getStyleClass().add("card-subtitulo");

		VBox card = new VBox(nombre, fechas);
		card.getStyleClass().add("card");

		card.setOnMouseClicked(event -> {

			session.setEspectaculoId(e.getId());

			if (session.getPerfil() == Perfil.COORDINACION && e.getCoordinacion().getId() != session.getUserId())
				openEspectaculoDetalle();
			else
				openEspectaculo();
		});

		return card;
	}

	private void openEspectaculo() {
		stageManager.switchScene(FxmlView.ESPECTACULO);
	}

	private void openEspectaculoDetalle() {
		stageManager.switchScene(FxmlView.ESPECTACULO_DETALLE);
	}

	@FXML
	private void showForm() {
		if (formularioBox.isVisible()) {
			formularioBox.setVisible(false);
			formularioBox.setManaged(false);
			listaBox.setMaxHeight(Double.MAX_VALUE);
			btnForm.setText("Nuevo Espectáculo");
		} else {
			formularioBox.setVisible(true);
			formularioBox.setManaged(true);
			listaBox.setMaxHeight(70);
			listaBox.setMinHeight(70);
			btnForm.setText("Ocultar Formulario");
		}
	}

	@FXML
	private void addNumero() {
		if (validarNumero()) {
			return;
		}

		Numero num = new Numero();
		num.setNombre(getNombreN());
		num.setDuracion(getDuracion());
		num.setArtistas(getArtistas());

		numSelected.add(num);

		limpiarNum();
	}

	private boolean validarNumero() {

		boolean nombre = getNombreN().isEmpty();
		txtNombreN.pseudoClassStateChanged(EMPTY, nombre);
		boolean inList = false;
		for (Numero n : numSelected) {
			if (n.getNombre().equals(getNombreN()))
				inList = true;
		}
		if (nuService.findByNombre(getNombreN()) != null || inList) {
			nombre = true;
			lblErrorNombreN.setText("Ya existe un número con ese nombre.");

			lblErrorNombreN.setManaged(nombre);
			lblErrorNombreN.setVisible(nombre);
		} else {
			lblErrorNombreN.setManaged(false);
			lblErrorNombreN.setVisible(false);
		}

		boolean duracion = spMinutos.getValue() == null;
		spMinutos.pseudoClassStateChanged(EMPTY, duracion);

		boolean artistas = getArtistas().isEmpty();
		lblErrorN.setText("Debe seleccionar al menos un artista que participará en el número.");

		lblErrorN.setManaged(artistas);
		lblErrorN.setVisible(artistas);

		return nombre || duracion || artistas;
	}

	@FXML
	private void numeroMoveUp() {
		int index = lvNumeros.getSelectionModel().getSelectedIndex();

		if (index > 0) {
			Collections.swap(numSelected, index, index - 1);
			lvNumeros.getSelectionModel().select(index - 1);
		}
	}

	@FXML
	private void numeroMoveDown() {
		int index = lvNumeros.getSelectionModel().getSelectedIndex();

		if (index != -1 && index < numSelected.size() - 1) {
			Collections.swap(numSelected, index, index + 1);
			lvNumeros.getSelectionModel().select(index + 1);
		}
	}

	@FXML
	private void eliminarNumero() {
		Numero seleccionado = lvNumeros.getSelectionModel().getSelectedItem();
		numSelected.remove(seleccionado);
	}

	@FXML
	public void limpiarForm() {
		txtNombre.clear();
		dpInicio.setValue(null);
		dpFin.setValue(null);
		cbCoordinador.setValue(null);
		numSelected.clear();
	}

	public void limpiarNum() {
		txtNombreN.clear();
		spMinutos.getValueFactory().setValue(1);
		cbDecimal.setValue(".0");
		checkArtistas.values().forEach(check -> check.setSelected(false));
	}

	@FXML
	public void save() {
		if (validarForm())
			return;

		Espectaculo es = new Espectaculo();

		es.setNombre(getNombre());
		es.setFechaini(getFechaIni());
		es.setFechafin(getFechaFin());
		es.setCoordinacion(getCoordinador());

		List<Long> numeroIds = new ArrayList<>();

		for (Numero n : numSelected) {
			Long id = nuService.save(n).getId();
			numeroIds.add(id);
		}

		esService.save(es, numeroIds);
		
		for (Numero n : numSelected) {
			for (Artista a : n.getArtistas()) {
				daService.actualizarTrayectoria(a.getId());
			}
		}
		limpiarForm();
		cargarEspectaculos();
	}

	private boolean validarForm() {

		boolean nombre = getNombre().isEmpty();
		txtNombre.pseudoClassStateChanged(EMPTY, nombre);
		if (getNombre().length() > 25) {
			nombre = true;
			lblErrorNombre.setText("El nombre no debe superar los 25 caracteres.");

			lblErrorNombre.setManaged(nombre);
			lblErrorNombre.setVisible(nombre);
		} else if (esService.findByNombre(getNombre()) != null) {
			nombre = true;
			lblErrorNombre.setText("Ya existe un espectáculo con ese nombre.");

			lblErrorNombre.setManaged(nombre);
			lblErrorNombre.setVisible(nombre);
		} else {
			lblErrorNombre.setManaged(false);
			lblErrorNombre.setVisible(false);
		}

		boolean fechaIni = getFechaIni() == null;
		dpInicio.pseudoClassStateChanged(EMPTY, fechaIni);

		boolean fechaFin = getFechaFin() == null;
		dpFin.pseudoClassStateChanged(EMPTY, fechaFin);

		boolean fechas = false;
		if (!fechaIni && !fechaFin) {
			if (getFechaFin().isBefore(getFechaIni())) {
				fechas = true;
				lblErrorFecha.setText("La fecha final no puede ser anterior a la fecha inicial.");
			} else if (getFechaIni().plusYears(1).isBefore(getFechaFin())) {
				fechas = true;
				lblErrorFecha.setText("La duración del espectáculo no debe ser superior a un año.");
			}

			lblErrorFecha.setManaged(fechas);
			lblErrorFecha.setVisible(fechas);
		} else {
			lblErrorFecha.setManaged(false);
			lblErrorFecha.setVisible(false);
		}

		boolean coordinador = false;
		if (session.getUserId() != null) {
			coordinador = getCoordinador() == null;
			cbCoordinador.pseudoClassStateChanged(EMPTY, coordinador);
		}

		boolean numeros = false;
		if (numSelected.size() < 3) {
			numeros = true;
			lblErrorNumeros.setText("Se deben seleccionar 3 números como mínimo.");

			lblErrorNumeros.setManaged(numeros);
			lblErrorNumeros.setVisible(numeros);
		} else {
			lblErrorNumeros.setManaged(false);
			lblErrorNumeros.setVisible(false);
		}

		return nombre || fechas || coordinador || numeros;
	}

	@FXML
	private void login() {
		stageManager.switchScene(FxmlView.LOGIN);
	}
}
