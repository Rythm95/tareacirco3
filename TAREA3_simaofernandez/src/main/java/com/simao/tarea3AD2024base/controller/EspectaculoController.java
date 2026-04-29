package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

@Controller
public class EspectaculoController implements Initializable {

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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (session.getEspectaculo() == null) {
			System.out.println("Error!!!");
		} else {
			cargarDatos(session.getEspectaculo());
			cargarCoordinadores();
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

	private void cargarDatos(Long idEs) {
		
		Espectaculo es = esService.find(idEs);  
		
		txtNombre.setPromptText(es.getNombre());
		txtNombre.setText(es.getNombre());
		dpInicio.setValue(es.getFechaini());
		dpInicio.setPromptText(es.getFechaini().toString());
		dpFin.setValue(es.getFechafin());
		dpFin.setPromptText(es.getFechafin().toString());
		cbCoordinador.setValue(es.getCoordinacion().getNombre());
		cbCoordinador.setPromptText(es.getCoordinacion().getNombre());

		//numSelected.clear();
		//lvNumeros.setItems(numSelected);
		List<Numero> numeros = es.getNumeros().stream().map(EspectaculoNumero::getNumero).toList();
		lvNumeros.getItems().setAll(numeros);
		//numSelected.addAll(numeros);
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
	private void save() {

	}

	@FXML
	private void reiniciarForm() {
		cargarDatos(session.getEspectaculo());
	}
}