package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.EspectaculoNumero;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.services.EspectaculoService;
import com.simao.tarea3AD2024base.services.PersonaService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

@Controller
public class VistaArtistaController implements Initializable {

	@FXML
	private VBox espectaculosContainer;

	@FXML
	private VBox numerosContainer;

	@FXML
	private VBox infoArtistaContainer;

	@FXML
	private Label lblNombre;

	@FXML
	private Label lblEmail;

	@FXML
	private Label lblNacionalidad;

	@FXML
	private Label lblApodo;

	@FXML
	private Label lblEspecialidades;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private EspectaculoService esService;

	@Autowired
	private PersonaService peService;

	@Autowired
	private Session session;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		List<Espectaculo> listaEspectaculos = esService.findAll();

		if (listaEspectaculos.isEmpty()) {
			Label vacio = new Label("No hay espectáculos programados.");
			espectaculosContainer.getChildren().add(vacio);
			return;
		}

		for (Espectaculo es : listaEspectaculos) {
			VBox card = espectaculoCard(es);
			espectaculosContainer.getChildren().add(card);
		}

		cargarFicha();

	}

	private VBox espectaculoCard(Espectaculo e) {

		Label nombre = new Label(e.getNombre());
		nombre.getStyleClass().add("card-titulo");

		Label fechas = new Label("Del " + e.getFechaini() + " al " + e.getFechafin());
		fechas.getStyleClass().add("card-subtitulo");

		VBox card = new VBox(nombre, fechas);
		
		card.getStyleClass().add("card");

		card.setOnMouseClicked(event -> {
			session.setEspectaculoId(e.getId());
			openEspectaculoDetalle();
		});

		return card;
	}

	private void openEspectaculoDetalle() {
		stageManager.switchScene(FxmlView.ESPECTACULO_DETALLE);
	}

	private void cargarFicha() {
		Artista a = peService.findArtistaCompleto(session.getPersonaId());

		lblNombre.setText("Nombre: " + a.getNombre());
		lblEmail.setText("Email: " + a.getEmail());
		lblNacionalidad.setText("Nacionalidad: " + a.getNacionalidad());
		if (!a.getApodo().isEmpty()) {
			lblApodo.setText("Apodo: " + a.getApodo());
		} else {
			lblApodo.setManaged(false);
			lblApodo.setVisible(false);
		}
		lblEspecialidades.setText("Especialidades: " + a.getEspecialidades().toString());

		for (Numero n : a.getNumeros()) {

			VBox numBox = new VBox(3);
			numBox.getStyleClass().add("card-invitado");

			Label lblNum = new Label("[Id " + n.getId() + "] " + n.getNombre());
			lblNum.getStyleClass().add("card-titulo");

			VBox espectaculosBox = new VBox(2);

			for (EspectaculoNumero en : n.getEspectaculos()) {
				Espectaculo es = en.getEspectaculo();

				String texto = "- [Id " + es.getId() + "] " + es.getNombre();
				espectaculosBox.getChildren().add(new Label(texto));
			}

			numBox.getChildren().addAll(lblNum, espectaculosBox);
			numerosContainer.getChildren().add(numBox);
		}

	}

	public void login() {
		stageManager.switchScene(FxmlView.LOGIN);
	}

	public void exit(ActionEvent event) {
		System.exit(0);
	}
}
