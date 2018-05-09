package com.segurosbolivar.asistencia.purecloud.monitor.negocio.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.api.RoutingApi;
import com.mypurecloud.sdk.v2.api.request.GetRoutingQueueUsersRequest;
import com.mypurecloud.sdk.v2.api.request.GetRoutingQueueUsersRequest.expandValues;
import com.mypurecloud.sdk.v2.api.request.GetRoutingQueuesRequest;
import com.mypurecloud.sdk.v2.model.Queue;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueMember;
import com.mypurecloud.sdk.v2.model.QueueMemberEntityListing;
import com.mypurecloud.sdk.v2.model.User;
import com.segurosbolivar.asistencia.purecloud.monitor.excepciones.PurecloudBusinessException;
import com.segurosbolivar.asistencia.purecloud.monitor.negocio.ICola;

@Stateless
public class ColaImpl implements ICola {

	/**
	 *
	 */
	private static final long serialVersionUID = -9086154316385042775L;
	private static final Logger logger = Logger.getLogger(ColaImpl.class);

	@Inject
	private ApiClient apiClient;

	@Override
	public List<User> obtenerUsuariosCola(String queueId, Boolean joined, List<expandValues> expand,
			List<String> presence) throws PurecloudBusinessException {
		logger.debug("obtenerUsuariosCola");

		QueueMemberEntityListing resultado = null;
		List<User> usuarios = new ArrayList<>();
		boolean hayUsuarios = true;

		RoutingApi apiInstance = new RoutingApi(apiClient);

		GetRoutingQueueUsersRequest.Builder builder = GetRoutingQueueUsersRequest.builder(queueId);

		builder.withPageNumber(1).withPageSize(25);

		if (joined != null)
			builder.withJoined(joined);

		if (expand != null && !expand.isEmpty())
			builder.withExpandEnumValues(expand);

		if (presence != null && !presence.isEmpty())
			builder.withPresence(presence);

		GetRoutingQueueUsersRequest request = builder.build();

		try {
			while (hayUsuarios) {
				resultado = apiInstance.getRoutingQueueUsers(request);

				if (resultado.getTotal() == 0l) {
					return usuarios;
				}

				for (QueueMember miembro : resultado.getEntities()) {
					usuarios.add(miembro.getUser());
				}

				if (resultado.getPageNumber().equals(resultado.getPageCount())) {
					hayUsuarios = false;
				} else {
					request.setPageNumber(request.getPageNumber() + 1);
				}
			}
		} catch (IOException | ApiException e) {
			throw new PurecloudBusinessException(e.getMessage(), e);
		}

		return usuarios;
	}

	@Override
	public List<Queue> obtenerColasDisponibles()throws PurecloudBusinessException {
		RoutingApi apiInstance = new RoutingApi(apiClient);
		GetRoutingQueuesRequest.Builder builder = GetRoutingQueuesRequest.builder();
		builder.withPageNumber(1).withPageSize(25).withActive(true);
		GetRoutingQueuesRequest request = builder.build();
		try {
			return apiInstance.getRoutingQueues(request).getEntities();
		}catch (IOException | ApiException e) {
			throw new PurecloudBusinessException(e.getMessage(), e);
		}
	}

}
