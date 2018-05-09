package com.segurosbolivar.asistencia.purecloud.monitor.utils;

import java.util.ResourceBundle;

/**
 * Clase para carga de propiedad desde archivo de propiedades
 * @author Asesoftware
 *
 */
public class UtilPropiedades {

	/**
	 * Obtener propiedad property en archivo de propiedades nomArch
	 * @param nomArch
	 * @param property
	 * @return
	 */
	public static String getPropertyValue(String nomArch, String property) {
		String keyValue = null;
		ResourceBundle resources = ResourceBundle.getBundle(nomArch);

		keyValue = resources.getString(property);

		return keyValue;
	}
}
