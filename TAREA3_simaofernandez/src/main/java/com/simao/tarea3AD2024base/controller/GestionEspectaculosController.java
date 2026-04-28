package com.simao.tarea3AD2024base.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.EspectaculoNumero;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.services.EspectaculoService;
import com.simao.tarea3AD2024base.services.NumeroService;
import com.simao.tarea3AD2024base.services.PersonaService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

	@EventListener
	public void onNewPersona(NewPersonaEvent event) {
		if (event.getPersona() instanceof Coordinacion)
			cargarCoordinadores();
	}

	@EventListener
	public void onNewNumero(NewNumeroEvent event) {
		cargarNumeros();
	}

	@FXML
	private VBox container;

	@FXML
	private ScrollPane formularioBox;

	@FXML
	private TextField txtNombre;

	@FXML
	private DatePicker dpInicio;

	@FXML
	private DatePicker dpFin;

	@FXML
	private ComboBox<String> cbNumeros;

	@FXML
	private ListView<Numero> lvNumeros;

	private ObservableList<Numero> numSelected = FXCollections.observableArrayList();

	@FXML
	private Label lblError;

	@FXML
	private Label lblErrorNombre;

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

	private LocalDate getFechaIni() {
		return dpInicio.getValue();
	}

	private LocalDate getFechaFin() {
		return dpFin.getValue();
	}

	private Coordinacion getCoordinador() {
		return (Coordinacion) peService.findByNombre(cbCoordinador.getValue());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cargarCoordinadores();
		cargarNumeros();
		cargarEspectaculos();

	}

	private void cargarCoordinadores() {
		List<Persona> listaCoordinacion = peService.findByPerfil(Perfil.COORDINACION);

		if (listaCoordinacion.isEmpty()) {
			save.setDisable(true);
			lblError.setText("Registra un coordinador antes de crear un espectáculo.");
			lblError.setVisible(true);
		} else {
			for (Persona p : listaCoordinacion) {
				cbCoordinador.getItems().add(p.getNombre());
			}
		}
	}

	private void cargarNumeros() {
		List<Numero> listaNumeros = nuService.findAll();

		if (listaNumeros.size() < 3 && !lblError.isVisible()) {
			save.setDisable(true);
			lblError.setText("Registra al menos 3 números circenses antes de crear un espectáculo.");
			lblError.setVisible(true);
		} else {
			lvNumeros.setItems(numSelected);
			for (Numero n : listaNumeros) {
				cbNumeros.getItems().add(n.getNombre());
			}

		}
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
			openEspectaculo(e);
		});

		return card;
	}

	private void openEspectaculo(Espectaculo e) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Detalle.fxml"));
			Parent root;

			root = loader.load();

			EspectaculoController controller = loader.getController();
			controller.setEspectaculo(e);

			Stage stage = new Stage();
			stage.setTitle(e.getNombre());
			stage.setScene(new Scene(root));
			
			stage.show();
			
		} catch (IOException er) {
			er.printStackTrace();
		}
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
	private void addNumero() {
		Numero num = nuService.findByNombre(cbNumeros.getValue());

		if (num != null) {
			boolean equals = false;
			for (Numero ns : numSelected) {
				if (ns.getId() == num.getId()) {
					equals = true;
					break;
				}
			}
			if (!equals) {
				numSelected.add(num);
			}
		}
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

		if (index < numSelected.size() - 1) {
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

	@FXML
	public void save() {
		if (validarForm()) {
			System.out.println("Maybe don't do that...");
			return;
		}
		System.out.println("Yeah, that's fine.");
		Espectaculo es = new Espectaculo();

		es.setNombre(getNombre());
		es.setFechaini(getFechaIni());
		es.setFechafin(getFechaFin());
		es.setIdCoordinacion(getCoordinador());

		List<EspectaculoNumero> numeros = new ArrayList<>();

		for (int i = 0; i < numSelected.size(); i++) {
			Numero num = numSelected.get(i);

			EspectaculoNumero esnu = new EspectaculoNumero();
			esnu.setEspectaculo(es);
			esnu.setNumero(num);
			esnu.setOrden(i + 1);

			numeros.add(esnu);
		}

		es.setNumeros(numeros);

		esService.save(es);
		limpiarForm();
		cargarEspectaculos();

	}

	private boolean validarForm() {

		boolean nombre = getNombre().isEmpty();
		txtNombre.pseudoClassStateChanged(EMPTY, nombre);
		if (esService.findByNombre(getNombre()) != null) {
			nombre = true;
			lblErrorNombre.setText("Ya existe un espectáculo con ese nombre.");
			System.out.println("Ya existe");

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

		boolean coordinador = getCoordinador() == null;
		cbCoordinador.pseudoClassStateChanged(EMPTY, coordinador);

		boolean numeros = false;
		if (numSelected.size() < 3) {
			numeros = true;
			lblErrorNumeros.setText("Se deben seleccionar 3 espectáculos como mínimo.");

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

	@FXML
	private void exit(ActionEvent event) {
		System.exit(0);
	}
}
