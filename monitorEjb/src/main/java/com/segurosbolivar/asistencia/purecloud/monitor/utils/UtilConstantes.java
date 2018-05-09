package com.segurosbolivar.asistencia.purecloud.monitor.utils;

/**
 * Constantes
 *
 * @author amessias
 *
 */
public final class UtilConstantes {

	private UtilConstantes() {
	}

	public static final String CLIENT_ID = "ad922c12-228d-4f02-afec-a85b294a0c0f";
	public static final String SECRET = "F3mET-1SxfPij0j77GWtNLd4jLTPKEJyIgfovVNbFpg";
	public static final String URL_TOKEN = "https://login.mypurecloud.com/token";

	public static final String TIMEZONE = "America/Bogota";
	public static final String TIMEZONE_UTC = "UTC";

	public static final String SCHEME = "https";
	public static final String HOST = "api.mypurecloud.com";

	public static final String APPLICATION_PROPERTIES = "application";
	public static final String COLAS_NOTIFICACIONES = "colas.notificaciones";
	public static final String COLAS_NOTIFICACIONES_EMAIL = "colas.notificaciones.email";
	public static final String COLAS_NOTIFICACIONES_INBOUND = "colas.notificaciones.inbound";
	public static final String COLAS_NOTIFICACIONES_OUTBOUND = "colas.notificaciones.outbound";
	public static final String PREFIX_URL_CHATQUERY = "https://apps.mypurecloud.com/directory/#/engage/admin/interactions/";

	public static final String CORREO_SERVICIO_CLIENTE = "correo.servicio.cliente";

	public static final String ACTIVO_CHAT = "notificaciones.chat";
	public static final String ACTIVO_EMAIL = "notificaciones.email";
	public static final String ACTIVO_CALL = "notificaciones.call";

	/**
	 * Topic Listeners
	 */
	public static final String TOPIC_CHAT = "v2.routing.queues.{0}.conversations.chats";
	public static final String TOPIC_EMAIL = "v2.routing.queues.{0}.conversations.emails";
	public static final String TOPIC_CALL = "v2.routing.queues.{0}.conversations.calls";

	/**
	 * Propiedades de parametros servicio SIEBEL Oportunidad Persona Natural (Ver Application.properties)
	 */
	public static final String SIEBEL_OPORTUNIDAD_NATURAL_URL = "siebel.servicio.oportunidad.natural.url";
	public static final String SIEBEL_OPORTUNIDAD_NATURAL_USER = "siebel.servicio.oportunidad.natural.user";
	public static final String SIEBEL_OPORTUNIDAD_NATURAL_PASSWD = "siebel.servicio.oportunidad.natural.password";
	public static final String SIEBEL_OPORTUNIDAD_NATURAL_SESSION = "siebel.servicio.oportunidad.natural.sessiontype";

	/**
	 * Propiedades de parametros servicio SIEBEL Solicitudes Persona Natural (Ver Application.properties)
	 */
	public static final String SIEBEL_SOLICITUD_NATURAL_URL = "siebel.servicio.solicitudes.natural.url";
	public static final String SIEBEL_SOLICITUD_NATURAL_USER = "siebel.servicio.solicitudes.natural.user";
	public static final String SIEBEL_SOLICITUD_NATURAL_PASSWD = "siebel.servicio.solicitudes.natural.password";

	/**
	 * Propiedades de paramettros servicio SIEBEL Interaccion Persona Natural (Ver Application.properties)
	 */
	public static final String SIEBEL_INTERACCION_NATURAL_URL = "siebel.servicio.interaccion.natural.url";
	public static final String SIEBEL_INTERACCION_NATURAL_USER = "siebel.servicio.interaccion.natural.user";
	public static final String SIEBEL_INTERACCION_NATURAL_PASSWORD = "siebel.servicio.interaccion.natural.password";
	public static final String SIEBEL_INTERACCION_NATURAL_SESSION = "siebel.servicio.interaccion.natural.sessiontype";

	public static final String CONSULTA_CLIENTE_HOST = "consulta.cliente.host";
	public static final String CONSULTA_CLIENTE_CORREO_PATH = "consulta.cliente.correo.path";
	public static final String CONSULTA_CLIENTE_DATOS_PATH = "consulta.cliente.datos.path";

	public static final String ACD_INTERACCIONES_ACTIVAS = "acd.maximo.interacciones.activas";
}
