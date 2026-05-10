/**
* Clase LogService.java
*
* @author Simao Fernandez Gervasoni
* @version 1.0
*/
package com.simao.tarea3AD2024base.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.simao.tarea3AD2024base.config.DB4OManager;
import com.simao.tarea3AD2024base.modelo.LogOperacion;
import com.simao.tarea3AD2024base.modelo.TipoOperacion;

@Service
public class LogOperacionService {

	public void newOperacion(String user, TipoOperacion tipo, String resumen) {
		ObjectContainer db = null;

		try {
			db = DB4OManager.open();

			long id = db.query(LogOperacion.class).size() + 1;

			LogOperacion log = new LogOperacion(id, LocalDateTime.now().toString(), user, tipo.name(), resumen);

			db.store(log);
			db.commit();

		} catch (Exception e) {
			if (db != null && !db.ext().isClosed())
				db.rollback();
			e.printStackTrace();

		} finally {
			if (db != null && !db.ext().isClosed())
				db.close();
		}

	}

	public List<LogOperacion> findAll() {
		ObjectContainer db = null;

		try {
			db = DB4OManager.open();

			ObjectSet<LogOperacion> result = db.query(LogOperacion.class);

			return new ArrayList<>(result);

		} finally {
			if (db != null)
				db.close();
		}

	}

	public List<LogOperacion> findFiltrados(String user, TipoOperacion operacion, LocalDate fechaIni, LocalDate fechaFin) {
		ObjectContainer db = null;

		try {
			db = DB4OManager.open();
			ObjectSet<LogOperacion> result = db.query(new Predicate<LogOperacion>() {

				@Override
				public boolean match(LogOperacion log) {
					boolean coincide = true;

					if (user != null && !user.isBlank()) {
						coincide = coincide && log.getUser().equalsIgnoreCase(user);
					}

					if (operacion != null) {
						coincide = coincide && log.getTipoOperacion().equalsIgnoreCase(operacion.name());
					}

					LocalDateTime fechaLog = LocalDateTime.parse(log.getFechaHora());
					if (fechaIni != null) {
					    coincide = coincide && !fechaLog.toLocalDate().isBefore(fechaIni);
					}

					if (fechaFin != null) {
						coincide = coincide && !fechaLog.toLocalDate().isAfter(fechaFin);
					}

					return coincide;
				}
			});

			return new ArrayList<>(result);

		} finally {
			if (db != null)
				db.close();
		}
	}
}
