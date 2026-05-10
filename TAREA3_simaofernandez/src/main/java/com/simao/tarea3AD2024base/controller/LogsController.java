package com.simao.tarea3AD2024base.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.simao.tarea3AD2024base.config.StageManager;
import com.simao.tarea3AD2024base.modelo.LogOperacion;
import com.simao.tarea3AD2024base.modelo.TipoOperacion;
import com.simao.tarea3AD2024base.services.LogOperacionService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Controller
public class LogsController implements Initializable {

	@FXML
	private TextField txtUsuario;

	@FXML
	private ComboBox<TipoOperacion> cbTipoOperacion;

	@FXML
	private DatePicker dpFechaIni;
	
	@FXML
	private DatePicker dpFechaFin;

	@FXML
	private TableView<LogOperacion> tvLogs;

	@FXML
	private TableColumn<LogOperacion, Long> colId;
	
	@FXML
	private TableColumn<LogOperacion, LocalDateTime> colFecha;
	
	@FXML
	private TableColumn<LogOperacion, String> colUsuario;
	
	@FXML 
	private TableColumn<LogOperacion, TipoOperacion> colTipo;
	
	@FXML
	private TableColumn<LogOperacion, String> colResumen;

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private LogOperacionService loService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbTipoOperacion.getItems().addAll(TipoOperacion.values());

		colId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
		colUsuario.setCellValueFactory(new PropertyValueFactory<>("user"));
		colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoOperacion"));
		colResumen.setCellValueFactory(new PropertyValueFactory<>("resumen"));
		
		cargarLogs();
	}
	
	@FXML
	public void limpiarFiltros() {
	    txtUsuario.clear();

	    cbTipoOperacion.setValue(null);
	    dpFechaIni.setValue(null);
	    dpFechaFin.setValue(null);
	    tvLogs.getItems().clear();

	    cargarLogs();
	}
	
	private void cargarLogs() {
	    tvLogs.getItems().clear();
	    tvLogs.getItems().addAll(loService.findAll());
	}
	
	@FXML
	private void filtrarLogs() {
		String user = txtUsuario.getText();
		TipoOperacion operacion = cbTipoOperacion.getValue();
		LocalDate fechaIni = dpFechaIni.getValue();
		LocalDate fechaFin = dpFechaFin.getValue();
		List<LogOperacion> logs = loService.findFiltrados(user, operacion, fechaIni, fechaFin);
		
		tvLogs.getItems().clear();
		tvLogs.getItems().addAll(logs);
	}

}
