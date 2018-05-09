package com.segurosbolivar.asistencia.purecloud.monitor.excepciones;

/**
 * Clase para excepciones de negocio
 *
 * @author amessias
 *
 */
public class PurecloudBusinessException extends Exception {

	private static final long serialVersionUID = 377410808834987583L;

	public PurecloudBusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public PurecloudBusinessException(String message) {
		super(message);
	}

	public PurecloudBusinessException(Throwable cause) {
		super(cause);
	}
}
