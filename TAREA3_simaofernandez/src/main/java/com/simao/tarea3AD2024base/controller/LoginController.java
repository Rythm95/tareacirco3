package com.simao.tarea3AD2024base.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.services.CredsService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class LoginController implements Initializable {

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());

	@FXML
	private PasswordField passwordField;

	@FXML
	private TextField usernameField;

	@FXML
	private Label lblError;

	@Autowired // Auto-conectar
	private CredsService credService;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@FXML
	private void login(ActionEvent event) throws IOException {

		Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
			properties.load(fis);
		} catch (IOException e) {
			logger.warning("Error al leer el fichero de propiedades: " + e.getMessage());
		}
		String user = properties.getProperty("usuarioAdmin");
		String pass = properties.getProperty("passwordAdmin");

		if (getPassword().isEmpty() || getUsername().isEmpty()) {
			if (getUsername().isEmpty())
				usernameField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
			if (getPassword().isEmpty())
				passwordField.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
		}
		else if (user.equals(getUsername()) && pass.equals(getPassword())) {
			stageManager.switchScene(FxmlView.ADMINISTRADOR);
		} else if (credService.authenticate(getUsername(), getPassword())) {

			List<Credenciales> creds = credService.findAll();
			String rol = "";
			for (Credenciales cr : creds) {

				if (cr.getUsername().equals(getUsername())) {

					rol = cr.getPerfil().toString();
					break;
				}
			}

			switch (rol) {
			case "ARTISTA":
				stageManager.switchScene(FxmlView.ARTISTA);
				break;

			case "COORDINADOR":
				stageManager.switchScene(FxmlView.COORDINADOR);
				break;

			}

		} else {
			lblError.setVisible(true);
		}
	}

	public String getPassword() {
		return passwordField.getText();
	}

	public String getUsername() {
		return usernameField.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void invitado() {
		stageManager.switchScene(FxmlView.INVITADO);
	}

}
