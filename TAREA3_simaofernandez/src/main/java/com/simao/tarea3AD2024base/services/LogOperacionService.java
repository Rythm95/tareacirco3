/**
* Clase LogService.java
*
* @author Simao Fernandez Gervasoni
* @version 1.0
*/
package com.simao.tarea3AD2024base.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.db4o.ObjectContainer;
import com.simao.tarea3AD2024base.config.DB4OManager;
import com.simao.tarea3AD2024base.modelo.LogOperacion;

@Service
public class LogOperacionService {
		
		public void newOperacion(String tipo, String resumen) {
			ObjectContainer db = null;
			
			try {
				db = DB4OManager.open();
				
				Long id = System.currentTimeMillis();
				
				LogOperacion log = new LogOperacion(id, LocalDateTime.now(), tipo, resumen);
				
				
				db.store(log);
				db.commit();

			}
			catch (Exception e) {
				if (db != null)
					db.rollback();
				
				e.printStackTrace();
			}
			finally {
				if (db != null)
					db.close();
			}
			
			
		}
}
