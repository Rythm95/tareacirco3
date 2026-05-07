/**
* Clase DB4OManager.java
*
* @author Simao Fernandez Gervasoni
* @version 1.0
*/
package com.simao.tarea3AD2024base.config;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class DB4OManager {

	private static final String pathDB4O = "ficheros/log.db4o";

	public static ObjectContainer open() {
		return Db4oEmbedded.openFile(pathDB4O);
	}

}
