package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
		}

	}

	private void cargarArtistas() {

		List<Persona> listaArtistas = peService.findByPerfil(Perfil.ARTISTA);
		List<Artista> ogArtistas = nuService.getNumeroWithArtistas(session.getNumeroId()).getArtistas();
		List<Long> ogIdArtistas = new ArrayList<>();

		for (Artista a : ogArtistas) {
			ogIdArtistas.add(a.getId());
		}
		
		checkArtistas.clear();
		containerArtistas.getChildren().clear();

		for (Persona p : listaArtistas) {
			Artista a = (Artista) p;
			CheckBox cb = new CheckBox(p.getNombre());
			checkArtistas.put(a, cb);
			containerArtistas.getChildren().add(cb);

			if (ogIdArtistas.contains(a.getId())) {
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
		
		cargarArtistas();
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
		nu.setDuracion(getDuracion());
		nu.setArtistas(getArtistas());

		nuService.update(session.getNumeroId(), nu);

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

		boolean artistas = getArtistas().isEmpty();
		lblError.setText("Debe seleccionar al menos un artista que participará en el número.");

		lblError.setManaged(artistas);
		lblError.setVisible(artistas);

		return nombre || artistas;
	}

	@FXML
	private void reiniciarForm() {
		cargarDatos();
	}
}