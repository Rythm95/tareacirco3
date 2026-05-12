package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.Numero;
import com.simao.tarea3AD2024base.modelo.Perfil;
import com.simao.tarea3AD2024base.modelo.Session;
import com.simao.tarea3AD2024base.services.EspectaculoService;
import com.simao.tarea3AD2024base.services.InformeXMLService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

@Controller
public class EspectaculoDetalleController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private Session session;

	@Autowired
	private EspectaculoService esService;
	
	@Autowired
	private InformeXMLService ixService;

	@FXML
	private Label lblId;

	@FXML
	private Label lblNombre;

	@FXML
	private Label lblFechas;

	@FXML
	private Label lblCoordNombre;

	@FXML
	private Label lblCoordEmail;

	@FXML
	private Label lblCoordSenior;

	@FXML
	private VBox numerosContainer;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (session.getEspectaculoId() == null) {
			goBack();
		} else {
			Espectaculo es = esService.getEspectaculoCompleto(session.getEspectaculoId());

			lblId.setText("Id: " + es.getId());
			lblNombre.setText("Nombre: " + es.getNombre());
			lblFechas.setText("Del " + es.getFechaini() + " al " + es.getFechafin() + ".");

			for (Numero n : es.getNumeros()) {

				VBox numeroCard = new VBox(5);
				numeroCard.getStyleClass().add("card-invitado");

				Label lblNum = new Label("[" + n.getId() + "] " + n.getNombre() + " (" + n.getDuracion() + " min)");
				Label lblArtistas = new Label("- Artistas:");

				VBox artistasBox = new VBox(3);

				for (Artista a : n.getArtistas()) {
					String texto = a.getNombre() + " - " + a.getNacionalidad();

					if (a.getApodo() != null && !a.getApodo().isEmpty()) {
						texto += " (" + a.getApodo() + ")";
					}

					texto += " | " + a.getEspecialidades();

					artistasBox.getChildren().add(new Label(texto));
				}

				numeroCard.getChildren().addAll(lblNum, lblArtistas, artistasBox);
				numerosContainer.getChildren().add(numeroCard);
			}

			Coordinacion c = es.getCoordinacion();

			lblCoordNombre.setText("Nombre: " + c.getNombre());
			lblCoordEmail.setText("Email: " + c.getEmail());
			lblCoordSenior.setText("Senior: " + (c.isSenior() ? "Sí" : "No"));

		}

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
	private void exportarEspectaculo() {
		Espectaculo es = esService.getEspectaculoCompleto(session.getEspectaculoId());
		ixService.generarInforme(es);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Exportación completada");
		alert.setHeaderText(null);
		alert.setContentText("El espectáculo se ha exportado correctamente.");
		alert.showAndWait();
	}
}