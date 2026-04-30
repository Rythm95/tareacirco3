package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
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

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@Controller
public class NumeroController implements Initializable {

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
	private Spinner<Integer> spMinutos;

	@FXML
	private ComboBox<String> cbDecimal;

	@FXML
	private VBox containerArtistas;

	private Map<Artista, CheckBox> checkArtistas = new HashMap<>();

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

	private double getDuracion() {
		double min = spMinutos.getValue();
		if (cbDecimal.getValue().equals(".5"))
			min += 0.5;
		return min;
	}

	private Numero getOgNumero() {
		return nuService.find(session.getNumeroId());
	}

	private List<Artista> getArtistas() {
		return checkArtistas.entrySet().stream().filter(es -> es.getValue().isSelected()).map(Map.Entry::getKey)
				.toList();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (session.getNumeroId() == null) {
			System.out.println("Error!!!");
		} else {
			spMinutos.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
			cbDecimal.getItems().addAll(".0", ".5");
			cbDecimal.getSelectionModel().selectFirst();

			cargarDatos();
			cargarArtistas();
		}

	}

	private void cargarArtistas() {

		List<Persona> listaArtistas = peService.findByPerfil(Perfil.ARTISTA);

		List<Artista> ogArtistas = getOgNumero().getArtistas();

		containerArtistas.getChildren().clear();

		for (Persona p : listaArtistas) {
			Artista a = (Artista) p;
			CheckBox cb = new CheckBox(p.getNombre());
			checkArtistas.put(a, cb);
			containerArtistas.getChildren().add(cb);

			if (ogArtistas.contains(a)) {
				cb.setSelected(true);
			}
		}

	}

	private void cargarDatos() {

		Numero nu = getOgNumero();

		txtNombre.setPromptText(nu.getNombre());
		txtNombre.setText(nu.getNombre());
		spMinutos.getValueFactory().setValue((int) nu.getDuracion());
		if (nu.getDuracion() % 2 != 0)
			cbDecimal.getSelectionModel().selectLast();
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
		if (newEspectaculo != null && !newEspectaculo.getId().equals(session.getEspectaculoId())) {
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
	private void reiniciarForm() {
		cargarDatos();
	}
}