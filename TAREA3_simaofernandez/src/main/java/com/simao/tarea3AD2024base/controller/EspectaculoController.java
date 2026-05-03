package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.EspectaculoNumero;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Persona;
import com.simao.tarea3AD2024base.modelo.Session;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@Controller
public class EspectaculoController implements Initializable {

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

	@Autowired
	private EspectaculoService esService;

	@FXML
	private TextField txtNombre;

	@FXML
	private DatePicker dpInicio;

	@FXML
	private DatePicker dpFin;

	@FXML
	private VBox coordinacionContainer;

	@FXML
	private ComboBox<String> cbCoordinador;

	@FXML
	private ComboBox<String> cbNumeros;

	@FXML
	private ListView<Numero> lvNumeros;

	private ObservableList<Numero> numSelected = FXCollections.observableArrayList();

	@FXML
	private Button save;

	@FXML
	private Label lblError;

	@FXML
	private Label lblErrorNombre;

	@FXML
	private Label lblErrorFecha;

	@FXML
	private Label lblErrorNumeros;

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
		if (session.getPerfil() == Perfil.COORDINACION)
			return (Coordinacion) peService.find(session.getPersonaId());
		else
			return (Coordinacion) peService.findByNombre(cbCoordinador.getValue());
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (session.getEspectaculoId() == null) {
			goBack();
		} else {
			cargarDatos();

			if (session.getPerfil() == Perfil.COORDINACION) {
				coordinacionContainer.setVisible(false);
				coordinacionContainer.setManaged(false);
			} else {
				cargarCoordinadores();
			}
			List<Numero> listaNumeros = nuService.findAll();
			for (Numero n : listaNumeros) {
				cbNumeros.getItems().add(n.getNombre());
			}
		}

	}

	private void cargarCoordinadores() {
		List<Persona> listaCoordinacion = peService.findByPerfil(Perfil.COORDINACION);

		if (listaCoordinacion.isEmpty()) {
			save.setDisable(true);
			lblError.setText("Error al cargar los coordinadores.");
			lblError.setVisible(true);
		} else {
			cbCoordinador.getItems().clear();
			for (Persona p : listaCoordinacion) {
				cbCoordinador.getItems().add(p.getNombre());
			}
		}
	}

	private void cargarDatos() {

		Espectaculo es = esService.find(session.getEspectaculoId());

		txtNombre.setPromptText(es.getNombre());
		txtNombre.setText(es.getNombre());
		dpInicio.setValue(es.getFechaini());
		dpInicio.setPromptText(es.getFechaini().toString());
		dpFin.setValue(es.getFechafin());
		dpFin.setPromptText(es.getFechafin().toString());
		cbCoordinador.setValue(es.getCoordinacion().getNombre());
		cbCoordinador.setPromptText(es.getCoordinacion().getNombre());

		numSelected.clear();
		lvNumeros.setItems(numSelected);
		List<Numero> numeros = esService.getNumerosFromEspectaculo(es.getId());
		numSelected.addAll(numeros);
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
		Espectaculo es = new Espectaculo();

		es.setNombre(getNombre());
		es.setFechaini(getFechaIni());
		es.setFechafin(getFechaFin());
		es.setCoordinacion(getCoordinador());

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

		esService.update(session.getEspectaculoId(), es);
		goBack();

	}

	private boolean validarForm() {

		boolean nombre = getNombre().isEmpty();
		txtNombre.pseudoClassStateChanged(EMPTY, nombre);
		Espectaculo newEspectaculo = esService.findByNombre(getNombre());
		if (getNombre().length() > 25) {
			nombre = true;
			lblErrorNombre.setText("El nombre no debe superar los 25 caracteres.");

			lblErrorNombre.setManaged(nombre);
			lblErrorNombre.setVisible(nombre);
		} else if (newEspectaculo != null && !newEspectaculo.getId().equals(session.getEspectaculoId())) {
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
		if (session.getPersonaId() != null) {
			coordinador = getCoordinador() == null;
			cbCoordinador.pseudoClassStateChanged(EMPTY, coordinador);
		}

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
	private void reiniciarForm() {
		cargarDatos();
	}
}