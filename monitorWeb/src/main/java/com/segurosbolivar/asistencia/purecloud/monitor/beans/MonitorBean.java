package com.segurosbolivar.asistencia.purecloud.monitor.beans;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.mypurecloud.sdk.v2.model.Queue;
import com.mypurecloud.sdk.v2.model.User;
import com.segurosbolivar.asistencia.purecloud.monitor.excepciones.PurecloudBusinessException;
import com.segurosbolivar.asistencia.purecloud.monitor.negocio.ICola;

@SessionScoped
@ManagedBean(name="monitor")
public class MonitorBean {

	private String idColaSeleccionada;
	private List<Queue> listaColas;
	private List<User> listaMiembros;

	@EJB
	private ICola colaService;

	@PostConstruct
	public void init() {
		listaColas = consultaColasActivas();
		listaMiembros = new ArrayList<>();
	}

	public void consultar() {
		this.listaMiembros = consultaUsuarios();
	}

	private List<User> consultaUsuarios() {
		if(idColaSeleccionada.isEmpty()) {
			return Collections.emptyList();
		}

		try {
			return colaService.obtenerUsuariosCola(idColaSeleccionada, true, null, Arrays.asList(new String[] {"ROUTING_STATUS"}));
		} catch (PurecloudBusinessException e) {

		}
		return Collections.emptyList();
	}

	private List<Queue> consultaColasActivas() {
		try {
			return colaService.obtenerColasDisponibles();
		} catch (PurecloudBusinessException e) {

		}
		return Collections.emptyList();
	}

	public String getIdColaSeleccionada() {
		return idColaSeleccionada;
	}

	public void setIdColaSeleccionada(String idColaSeleccionada) {
		this.idColaSeleccionada = idColaSeleccionada;
	}

	public List<Queue> getListaColas() {
		return listaColas;
	}

	public void setListaColas(List<Queue> listaColas) {
		this.listaColas = listaColas;
	}

	public List<User> getListaMiembros() {
		return listaMiembros;
	}

	public void setListaMiembros(List<User> listaMiembros) {
		this.listaMiembros = listaMiembros;
	}




}
