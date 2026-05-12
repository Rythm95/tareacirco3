package com.simao.tarea3AD2024base.services;

import java.io.File;
import java.time.LocalDateTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.simao.tarea3AD2024base.config.EXistDBManager;
import com.simao.tarea3AD2024base.modelo.Artista;
import com.simao.tarea3AD2024base.modelo.Coordinacion;
import com.simao.tarea3AD2024base.modelo.Espectaculo;
import com.simao.tarea3AD2024base.modelo.Numero;

@Service
public class InformeXMLService {

	@Autowired
	private EXistDBManager exManager;

	public void generarInforme(Espectaculo es) {

		try {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document doc = builder.newDocument();

			Element root = doc.createElement("informe");
			doc.appendChild(root);

			Element fechaHora = doc.createElement("fechahora");
			fechaHora.setTextContent(LocalDateTime.now().toString());
			root.appendChild(fechaHora);

			Element espectaculo = doc.createElement("espectaculo");
			root.appendChild(espectaculo);

			crearElemento(doc, espectaculo, "id", es.getId().toString());
			crearElemento(doc, espectaculo, "nombre", es.getNombre());
			crearElemento(doc, espectaculo, "fechaini", es.getFechaini().toString());
			crearElemento(doc, espectaculo, "fechafin", es.getFechafin().toString());

			Coordinacion c = es.getCoordinacion();
			Element coordinacion = doc.createElement("coordinacion");
			espectaculo.appendChild(coordinacion);

			crearElemento(doc, coordinacion, "nombre", c.getNombre());
			crearElemento(doc, coordinacion, "email", c.getEmail());
			crearElemento(doc, coordinacion, "senior", String.valueOf(c.isSenior()));

			Element numeros = doc.createElement("numeros");
			espectaculo.appendChild(numeros);

			for (Numero n : es.getNumeros()) {

				Element numero = doc.createElement("numero");
				numeros.appendChild(numero);

				crearElemento(doc, numero, "orden", String.valueOf(n.getOrden()));
				crearElemento(doc, numero, "nombre", n.getNombre());
				crearElemento(doc, numero, "duracion", String.valueOf(n.getDuracion()));

				Element artistas = doc.createElement("artistas");
				numero.appendChild(artistas);

				for (Artista a : n.getArtistas()) {

					Element artista = doc.createElement("artista");
					artistas.appendChild(artista);

					crearElemento(doc, artista, "nombre", a.getNombre());
					crearElemento(doc, artista, "nacionalidad", a.getNacionalidad());
					crearElemento(doc, artista, "email", a.getEmail());
					crearElemento(doc, artista, "especialidades", a.getEspecialidades().toString());

					if (a.getApodo() != null && !a.getApodo().isBlank()) {

						crearElemento(doc, artista, "apodo", a.getApodo());
					}
				}
			}

			File carpeta = new File("ficheros");

			if (!carpeta.exists())
				carpeta.mkdirs();

			String nombreArchivo = "informe_espectaculo_" + es.getId() + ".xml";
			File archivo = new File(carpeta, nombreArchivo);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(archivo);
			transformer.transform(source, result);

			exManager.guardarDocumento(archivo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void crearElemento(Document doc, Element padre, String tag, String valor) {

		Element elemento = doc.createElement(tag);

		elemento.setTextContent(valor);

		padre.appendChild(elemento);
	}
}