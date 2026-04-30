package com.simao.tarea3AD2024base.view;

import java.util.ResourceBundle;

public enum FxmlView {
	// Añadir un nuevo enum para añadir pantalla.fxml, con un nuevo pantalla.title
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
	},
	ESPECTACULO {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("espectaculo.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Espectaculo.fxml";
		}
		
	},
	NUMERO {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("numero.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Numero.fxml";
		}
	},
	PERSONA_COORDINACION {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("coordinacion.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Persona.fxml";
		}
	},
	PERSONA_ARTISTA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("artista.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Persona.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
