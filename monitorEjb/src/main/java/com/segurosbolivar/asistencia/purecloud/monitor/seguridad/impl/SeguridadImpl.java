package com.segurosbolivar.asistencia.purecloud.monitor.seguridad.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.segurosbolivar.asistencia.purecloud.monitor.dto.TokenAccess;
import com.segurosbolivar.asistencia.purecloud.monitor.excepciones.PurecloudBusinessException;
import com.segurosbolivar.asistencia.purecloud.monitor.seguridad.ISeguridad;
import com.segurosbolivar.asistencia.purecloud.monitor.utils.UtilServicios;

import static com.segurosbolivar.asistencia.purecloud.monitor.utils.UtilConstantes.CLIENT_ID;
import static com.segurosbolivar.asistencia.purecloud.monitor.utils.UtilConstantes.URL_TOKEN;
import static com.segurosbolivar.asistencia.purecloud.monitor.utils.UtilConstantes.SECRET;

@Stateless
public class SeguridadImpl implements ISeguridad {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(SeguridadImpl.class);

	@Override
	public String obtenerToken() throws PurecloudBusinessException {
		logger.debug("obtenerToken");
		TokenAccess token = new TokenAccess();

		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(URL_TOKEN);

		String userCredentials = CLIENT_ID + ":" + SECRET;

		String basicAuth = "Basic " + new String(new Base64().encode(userCredentials.getBytes()));
		post.setHeader("Authorization", basicAuth);

		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));

		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters);
			post.setEntity(entity);

			HttpResponse response = client.execute(post);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuilder result = new StringBuilder();
			String line = "";

			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			logger.debug("TOKEN: " + result.toString());

			token = UtilServicios.parse(result.toString(), TokenAccess.class);
		} catch (Exception e) {
			throw new PurecloudBusinessException(e);
		}

		return token.getAccessToken();
	}

}
