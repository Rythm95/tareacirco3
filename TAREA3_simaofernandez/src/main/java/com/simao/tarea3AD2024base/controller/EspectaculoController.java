package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

@Controller
public class EspectaculoController implements Initializable{

	@FXML
    private TextField txtNombre;

    @FXML
    private DatePicker dpInicio;

    @FXML
    private DatePicker dpFin;

    private Espectaculo espectaculo;

    public void setEspectaculo(Espectaculo espectaculo) {
    	this.espectaculo = espectaculo;
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}