package com.simao.tarea3AD2024base.view;

import java.util.ResourceBundle;

public enum FxmlView {
	USER {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("user.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/User.fxml";
		}
	}, // Añadir un nuevo enum para añadir pantalla.fxml, con un nuevo pantalla.title
	ADMINISTRADOR {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("admin.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Admin.fxml";
		}
	},
	COORDINADOR {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("coordinador.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Coordinador.fxml";
		}
	},
	ARTISTA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("artista.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Artista.fxml";
		}
	},
	INVITADO {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("invitado.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Invitado.fxml";
		}
	},
	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Login.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
