package com.segurosbolivar.asistencia.purecloud.monitor.negocio;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.apache.log4j.Logger;

import com.mypurecloud.sdk.v2.ApiClient;
import com.segurosbolivar.asistencia.purecloud.monitor.excepciones.PurecloudBusinessException;
import com.segurosbolivar.asistencia.purecloud.monitor.seguridad.ISeguridad;

@Singleton
@Startup
public class Configurador {

	private static Logger logger = Logger.getLogger(Configurador.class);

	private volatile String tokenUsuario;
	private static final String BASICAUTH = "Bearer ";

	@EJB
	private ISeguridad seguridadService;

	@Schedule(second = "0", minute = "0", hour = "11", persistent = false)
	private void cargarToken() {
		try {
			tokenUsuario = seguridadService.obtenerToken();
		} catch (PurecloudBusinessException e) {
			tokenUsuario = null;
			logger.error(e.getMessage());
		}
	}

	public String getTokenUsuario() {
		if (tokenUsuario == null) {
			cargarToken();
		}

		return tokenUsuario;
	}

	@Produces
	@RequestScoped
	public ApiClient getApiClient() {
		return ApiClient.Builder.standard().withBasePath("https://api.mypurecloud.com")
				.withDefaultHeader("Authorization", BASICAUTH + getTokenUsuario()).build();
	}

}
