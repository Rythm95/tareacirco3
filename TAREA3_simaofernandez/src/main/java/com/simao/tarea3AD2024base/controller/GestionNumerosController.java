package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.services.NumeroService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

@Controller
public class GestionNumerosController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private NumeroService nuService;

	@Autowired
	private Session session;

	@FXML
	private VBox container;

	@FXML
	private ScrollPane listaBox;

	@FXML
	private VBox containerArtistas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cargarNumeros();
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

}