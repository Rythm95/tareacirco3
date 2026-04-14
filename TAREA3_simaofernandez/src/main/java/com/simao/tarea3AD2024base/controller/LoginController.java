package com.simao.tarea3AD2024base.controller;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Credenciales;
import com.simao.tarea3AD2024base.modelo.User;
import com.simao.tarea3AD2024base.services.CredsService;
import com.simao.tarea3AD2024base.services.UserService;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * @author Ram Alapure
 * @since 05-04-2017
 */

@Controller
public class LoginController implements Initializable{

	@FXML
    private Button btnLogin;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label lblLogin;
    
    @Autowired // Auto-conectar
    private UserService userService;
    
    @Lazy
    @Autowired
    private StageManager stageManager;
        
	@FXML
    private void login(ActionEvent event) throws IOException{
    	if(CredsService.authenticate(getUsername(), getPassword())){
    		    		
    		List<Credenciales> users = userService.findAll();
			String rol = "";
			for (User u : users) {

				if (u.getEmail().equals(getUsername())) {
					rol = u.getRole();
					break;
				}
			}

			switch (rol) {
			case "Admin":
				stageManager.switchScene(FxmlView.ADMINISTRADOR);
				break;
				
			case "Artista":
				stageManager.switchScene(FxmlView.ARTISTA);
				break;
				
			case "Coordinador":
				stageManager.switchScene(FxmlView.COORDINADOR);
				break;

			}
    		
    	}else{
    		lblLogin.setText("Error al iniciar sesión.");
    	}
    }
	
	public String getPassword() {
		return password.getText();
	}

	public String getUsername() {
		return username.getText();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public void invitado() {
		stageManager.switchScene(FxmlView.INVITADO);
	}

}
