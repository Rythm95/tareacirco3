/**
* Clase AccesoPaises.java
*
* @author Simao Fernandez Gervasoni
* @version 1.0
*/
package com.simao.tarea3AD2024base.services;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AccesoPaises {

	public static Map<String, String> loadPaises() {

		Map<String, String> paises = new LinkedHashMap<>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse("src/main/resources/paises.xml");
			document.getDocumentElement().normalize();

			NodeList list = document.getElementsByTagName("pais");

			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;
					String id = element.getElementsByTagName("id").item(0).getTextContent();
					String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
					paises.put(id, nombre);
				}
			}

		} catch (ParserConfigurationException | IOException | SAXException e) {
			System.out.println("Ha habido un error al intentar leer el archivo paises.xml");
		}
		return paises;

	}
}
