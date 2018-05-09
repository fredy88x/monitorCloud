package com.segurosbolivar.asistencia.purecloud.monitor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.segurosbolivar.asistencia.purecloud.monitor.excepciones.PurecloudBusinessException;

/**
 * Clase utilidad para los servicios
 *
 * @author amessias
 *
 */
public final class UtilServicios {

	private static final String TIPO_AUTORIZACION = "Bearer ";

	public static enum Method {
		GET, POST
	};

	private UtilServicios() {
	}

	public static <T extends Object> T parse(String json, Class<T> clase)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(json, clase);
	}

	public static <T extends Object> List<T> parseList(String json, Class<T> clase)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, clase));
	}

	/**
	 * Request GET o POST
	 *
	 * @param host
	 *            Host
	 * @param path
	 *            Camino REST
	 * @param id
	 *            Identificador, utilizado para GET
	 * @param token
	 *            Token de autenticación
	 * @param metodo
	 *            GET o POST
	 * @param body
	 *            Cuerpo del request
	 * @param params
	 *            Parametros
	 * @param retorno
	 *            Clase de retorno
	 * @return Clase
	 * @throws URISyntaxException
	 * @throws UnsupportedOperationException
	 * @throws IOException
	 * @throws PurecloudBusinessException
	 */
	public static <T extends Object> T submit(String host, String path, String id, String token, Method metodo,
			String body, Map<String, Object> params, Class<T> retorno)
			throws URISyntaxException, UnsupportedOperationException, IOException, PurecloudBusinessException {

		StringBuilder url = new StringBuilder();

		if (id != null && !id.isEmpty()) {
			url.append(MessageFormat.format(path, id));
		} else {
			url.append(path);
		}

		URIBuilder builder = new URIBuilder();
		builder.setScheme(UtilConstantes.SCHEME).setHost(host).setPath(url.toString());

		if (params != null) {
			for (Entry<String, Object> par : params.entrySet()) {
				builder.setParameter(par.getKey(), par.getValue().toString());
			}
		}

		HttpUriRequest request = null;

		if (Method.GET.equals(metodo)) {
			request = new HttpGet(builder.build());
		} else {
			request = new HttpPost(builder.build());
			request.setHeader("Content-Type", "application/json; charset=UTF-8");

			if (body != null) {
				HttpEntity entity = new ByteArrayEntity(body.getBytes("UTF-8"));
				((HttpPost) request).setEntity(entity);
			}
		}

		if (token != null) {
			request.setHeader("Authorization", TIPO_AUTORIZACION + token);
		}

		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));

		StringBuffer result = new StringBuffer();
		String line = "";

		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new PurecloudBusinessException(result.toString());
		}

		return parse(result.toString(), retorno);
	}

	/**
	 * Obtener un map de parametros a partir de un dto
	 *
	 * @param dto
	 *            Dto
	 * @return Map con los parametros no nulos
	 */
	public static Map<String, Object> parseParametros(Object dto) {
		Map<String, Object> params = new HashMap<String, Object>();

		for (Class<?> cls = dto.getClass(); cls != null && cls != Object.class; cls = cls.getSuperclass()) {
			for (Field f : cls.getDeclaredFields()) {
				f.setAccessible(true);

				try {
					if (f.getName() != "token" && f.getName() != "serialVersionUID" && f.get(dto) != null)
						params.put(f.getName(), f.get(dto));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

		return params;
	}

	/**
	 * Parser de una fecha para String ISO8601
	 *
	 * @param fecha
	 *            Fecha
	 * @return fecha formateada
	 */
	public static String parseISO8601(Date fecha) {
		TimeZone tz = TimeZone.getTimeZone(UtilConstantes.TIMEZONE);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		df.setTimeZone(tz);

		return df.format(fecha);
	}

	/**
	 * Parser LocalDateTime para String ISO8601
	 *
	 * @param fecha Fecha
	 * @return fecha formateada
	 */
	public static String parseISO8601(LocalDateTime fecha) {
		ZonedDateTime oo = ZonedDateTime.of(fecha, ZoneOffset.UTC);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

		return format.format(oo);
	}

	/**
	 * Parse de String a java.util.Date
	 *
	 * @param fecha
	 * @return
	 * @throws ParseException
	 */
	public static Date parseStringToDate(String fecha) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone(UtilConstantes.TIMEZONE));

		return df.parse(fecha);
	}

	/**
	 * Parse de String a java.util.Date ISO8601
	 *
	 * @param fecha
	 * @return
	 * @throws ParseException
	 */
	public static Date parseStringToDateISO8601(String fecha) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
		df.setTimeZone(TimeZone.getTimeZone(UtilConstantes.TIMEZONE_UTC));

		return df.parse(fecha);
	}

	public static Date parseStringToDateISO8601SinLocale(String fecha) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

		return df.parse(fecha);
	}

	/**
	 * Parse de una fecha String a String
	 *
	 * @param fecha
	 * @return
	 * @throws ParseException
	 */
	public static String parseStringDate(String fecha) throws ParseException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		df.setTimeZone(TimeZone.getTimeZone(UtilConstantes.TIMEZONE));

		return df.format(fecha);
	}

	/**
	 * Parse de una fecha ISO8601 en UTC
	 *
	 * @param fecha
	 * @return
	 * @throws ParseException
	 */
	public static Date parseStringDateISO8601ToUTC(String fecha) throws ParseException {
		Date aux = UtilServicios.parseStringToDateISO8601SinLocale(fecha);

		Calendar local = Calendar.getInstance();
		local.setTime(aux);
		local.setTimeInMillis(local.getTimeInMillis()
				- TimeZone.getTimeZone(UtilConstantes.TIMEZONE).getOffset(local.getTimeInMillis()));
		return local.getTime();
	}

	/**
	 * Parse un Objeto para JSON
	 *
	 * @param pojo
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String parseObjetoParaJSON(Object pojo) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(pojo);
	}

	/**
	 * Parse un Long a hh:mm:ss
	 *
	 * @param milis
	 *            Milisegundos
	 * @return Hora formateada
	 */
	public static String parseMilisegundosParaString(Long milis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(milis);
		cal.setTimeZone(TimeZone.getTimeZone(UtilConstantes.TIMEZONE_UTC));

		int seconds = (int) (cal.getTimeInMillis() / 1000) % 60;
		int minutes = (int) ((cal.getTimeInMillis() / (1000 * 60)) % 60);
		int hours = (int) ((cal.getTimeInMillis() / (1000 * 60 * 60)));

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}

	public static List<Date> obtenerDiasEntreFechas(Date inicio, Date fin) {
		List<Date> dates = new ArrayList<Date>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(inicio);

		while (calendar.getTime().compareTo(fin) <= 0) {
			Date result = calendar.getTime();
			dates.add(result);
			calendar.add(Calendar.DATE, 1);
		}

		return dates;
	}
}
