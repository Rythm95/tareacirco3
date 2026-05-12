package com.simao.tarea3AD2024base.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import jakarta.annotation.PostConstruct;

@Component
public class EXistDBManager {

	@Value("${existdb.uri}")
	private String uri;

	@Value("${existdb.user}")
	private String user;

	@Value("${existdb.password}")
	private String password;

	@Value("${existdb.collection}")
	private String collectionPath;

	@PostConstruct
	public void init() {
		try {
			Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
			Database database = (Database) cl.getDeclaredConstructor().newInstance();

			DatabaseManager.registerDatabase(database);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Collection createCollection() throws Exception {

		Collection col = DatabaseManager.getCollection(uri + collectionPath, user, password);

		if (col == null) {
			Collection root = DatabaseManager.getCollection(uri + "/db", user, password);

			CollectionManagementService mgtService = (CollectionManagementService) root
					.getService("CollectionManagementService", "1.0");

			mgtService.createCollection("informes");

			col = DatabaseManager.getCollection(uri + collectionPath, user, password);
		}

		return col;
	}

	public void guardarDocumento(File file) {

		Collection col = null;

		try {

			col = createCollection();

			XMLResource resource = (XMLResource) col.createResource(file.getName(), "XMLResource");

			resource.setContent(file);

			col.storeResource(resource);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			try {
				if (col != null)
					col.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
