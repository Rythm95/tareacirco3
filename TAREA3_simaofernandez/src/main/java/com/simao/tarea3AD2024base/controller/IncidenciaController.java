package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.objectdb.modelo.Incidencia;
import com.simao.tarea3AD2024base.objectdb.modelo.TipoIncidencia;
import com.simao.tarea3AD2024base.services.EspectaculoService;
import com.simao.tarea3AD2024base.services.IncidenciaService;
import com.simao.tarea3AD2024base.services.NumeroService;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

@Component
public class IncidenciaController implements Initializable {

	private PseudoClass EMPTY = PseudoClass.getPseudoClass("error");

	@Autowired
	private IncidenciaService inService;

	@Autowired
	private EspectaculoService esService;

	@Autowired
	private NumeroService nuService;

	@Autowired
	private Session session;

	@FXML
	private VBox formRegistro;

	@FXML
	private VBox formConsulta;

	@FXML
	private Button btnForms;

	@FXML
	private ComboBox<TipoIncidencia> cbTipo;

	@FXML
	private ComboBox<Long> cbIdEspectaculo;

	@FXML
	private ComboBox<Long> cbIdNumero;

	@FXML
	private TextArea txtDescripcion;

	@FXML
	private Label lblRegistro;

	@FXML
	private ComboBox<String> cbFiltroTipo;

	@FXML
	private ComboBox<String> cbFiltroEstado;

	@FXML
	private DatePicker dpFechaInicio;

	@FXML
	private DatePicker dpFechaFin;

	@FXML
	private ComboBox<Long> cbFiltroEspectaculo;

	@FXML
	private ComboBox<Long> cbFiltroNumero;

	@FXML
	private TableView<Incidencia> tablaIncidencias;

	@FXML
	private TableColumn<Incidencia, Long> colId;

	@FXML
	private TableColumn<Incidencia, String> colFecha;

	@FXML
	private TableColumn<Incidencia, String> colTipo;

	@FXML
	private TableColumn<Incidencia, String> colDescripcion;

	@FXML
	private TableColumn<Incidencia, Boolean> colResuelta;

	@FXML
	private TableColumn<Incidencia, String> colPersona;

	@FXML
	private TableColumn<Incidencia, Long> colEspectaculo;

	@FXML
	private TableColumn<Incidencia, Long> colNumero;

	@FXML
	private VBox formResolve;

	@FXML
	private TextArea txtResolucion;

	@FXML
	private Label lblResolucion;

	@FXML
	private Label lblErrorIncidencia;

	private Long getIdEspectaculo() {
		Long id = cbIdEspectaculo.getValue();

		if (id == null || id == 0)
			return null;

		return id;
	}

	private Long getIdNumero() {
		Long id = cbIdNumero.getValue();

		if (id == null || id == 0)
			return null;

		return id;
	}

	private Long getFiltroEspectaculo() {
		Long id = cbFiltroEspectaculo.getValue();

		if (id == null || id == 0)
			return null;

		return id;
	}

	private Long getFiltroNumero() {
		Long id = cbFiltroNumero.getValue();

		if (id == null || id == 0)
			return null;

		return id;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		if (session.getPerfil() == Perfil.ARTISTA) {
			formResolve.setVisible(false);
			formResolve.setManaged(false);
		}

		cbTipo.getItems().addAll(TipoIncidencia.values());
		cbFiltroTipo.getItems().addAll("TODOS", "TECNICA", "ARTISTICA", "ORGANIZATIVA");
		cbFiltroTipo.setValue("TODOS");
		cbFiltroEstado.getItems().addAll("TODAS", "RESUELTAS", "NO RESUELTAS");
		cbFiltroEstado.setValue("TODAS");

		List<Espectaculo> listaEspectaculos = esService.findAll();
		for (Espectaculo es : listaEspectaculos) {
			cbIdEspectaculo.getItems().add(es.getId());
			cbFiltroEspectaculo.getItems().add(es.getId());
		}
		List<Numero> listaNumeros = nuService.findAll();
		for (Numero n : listaNumeros) {
			cbIdNumero.getItems().add(n.getId());
			cbFiltroNumero.getItems().add(n.getId());
		}

		cargarTabla();
		cargarIncidencias();
	}

	private void cargarTabla() {

		colId.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getId()).asObject());
		colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaHora().toString()));
		colFecha.setCellValueFactory(data -> {
			String fecha = data.getValue().getFechaHora().toString();
			if (fecha == null)
				return new SimpleStringProperty("");
			return new SimpleStringProperty(fecha.substring(0, 16).replace("T", " "));
		});
		colTipo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTipo().toString()));
		colDescripcion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescripcion()));
		colResuelta.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isResuelta()).asObject());
		colPersona.setCellValueFactory(data -> {

			Long id = data.getValue().getIdPersonaReporta();

			if (id == null)
				return new SimpleStringProperty("ADMIN");

			return new SimpleStringProperty(id.toString());
		});

		colEspectaculo.setCellValueFactory(data -> {
			Long id = data.getValue().getIdEspectaculo();

			if (id == null)
				id = 0L;

			return new SimpleLongProperty(id).asObject();
		});

		colNumero.setCellValueFactory(data -> {

			Long id = data.getValue().getIdNumero();

			if (id == null)
				id = 0L;

			return new SimpleLongProperty(id).asObject();
		});
	}

	private void cargarIncidencias() {

		tablaIncidencias.setItems(FXCollections.observableArrayList(inService.findAll()));
	}

	@FXML
	public void registrarIncidencia() {

		lblRegistro.setText("");

		boolean tipo = cbTipo.getValue() == null;
		cbTipo.pseudoClassStateChanged(EMPTY, tipo);

		boolean desc = txtDescripcion.getText().isBlank();
		txtDescripcion.pseudoClassStateChanged(EMPTY, desc);
		if (!desc) {
			if (txtDescripcion.getText().length() > 1000) {
				desc = true;
				lblErrorIncidencia.setText("La descripción no puede contener más de 1000 caracteres");
			}
			lblErrorIncidencia.setManaged(desc);
			lblErrorIncidencia.setVisible(desc);
		} else {
			lblErrorIncidencia.setManaged(false);
			lblErrorIncidencia.setVisible(false);
		}

		if (tipo || desc)
			return;

		Long idEspectaculo = getIdEspectaculo();

		Long idNumero = getIdNumero();

		inService.registrar(cbTipo.getValue(), txtDescripcion.getText(), idEspectaculo, idNumero);

		limpiarRegistro();

		cargarIncidencias();

		lblRegistro.setText("Incidencia registrada.");
	}

	private void limpiarRegistro() {

		cbTipo.setValue(null);
		txtDescripcion.clear();
		cbIdEspectaculo.setValue(null);
		cbIdNumero.setValue(null);
	}

	@FXML
	public void buscarIncidencias() {

		TipoIncidencia tipo = null;

		if (!cbFiltroTipo.getValue().equals("TODOS"))
			tipo = TipoIncidencia.valueOf(cbFiltroTipo.getValue());

		Boolean resuelta = null;

		if (cbFiltroEstado.getValue().equals("RESUELTAS"))
			resuelta = true;
		else if (cbFiltroEstado.getValue().equals("NO RESUELTAS"))
			resuelta = false;

		Long idEspectaculo = getFiltroEspectaculo();
		Long idNumero = getFiltroNumero();
		LocalDate fechaIni = dpFechaInicio.getValue();
		LocalDate fechaFin = dpFechaFin.getValue();

		List<Incidencia> lista = inService.findIncidenciaFiltro(tipo, resuelta, idEspectaculo, idNumero, fechaIni,
				fechaFin);

		tablaIncidencias.setItems(FXCollections.observableArrayList(lista));
	}

	@FXML
	public void resolverIncidencia() {

		lblResolucion.setText("");

		Incidencia i = tablaIncidencias.getSelectionModel().getSelectedItem();

		if (i == null) {
			lblResolucion.setText("Selecciona una incidencia.");
			return;
		}

		if (i.isResuelta()) {
			lblResolucion.setText("La incidencia ya está resuelta.");
			return;
		}

		boolean resolution = txtResolucion.getText().isBlank();
		txtResolucion.pseudoClassStateChanged(EMPTY, resolution);

		if (resolution)
			return;

		inService.resolver(i, txtResolucion.getText());

		txtResolucion.clear();

		cargarIncidencias();

		lblResolucion.setText("Incidencia resuelta.");
	}

	@FXML
	private void switchForms() {
		boolean b = formRegistro.isVisible();
		formRegistro.setVisible(!b);
		formRegistro.setManaged(!b);
		formConsulta.setVisible(b);
		formConsulta.setManaged(b);
		btnForms.setText(b ? "Registrar incidencias" : "Consultar incidencias");
	}

	@FXML
	public void limpiarFiltros() {

		cbFiltroTipo.setValue("TODOS");
		cbFiltroEstado.setValue("TODAS");
		dpFechaInicio.setValue(null);
		dpFechaFin.setValue(null);
		cbFiltroEspectaculo.setValue(null);
		cbFiltroNumero.setValue(null);

		cargarIncidencias();
	}

}
