package com.segurosbolivar.asistencia.purecloud.monitor.negocio;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;

import com.mypurecloud.sdk.v2.model.Queue;
import com.mypurecloud.sdk.v2.model.User;
import com.segurosbolivar.asistencia.purecloud.monitor.excepciones.PurecloudBusinessException;
import com.mypurecloud.sdk.v2.api.request.GetRoutingQueueUsersRequest.expandValues;

@Local
public interface ICola extends Serializable {

	List<User> obtenerUsuariosCola(String queueId, Boolean joined, List<expandValues> expand, List<String> presence)throws PurecloudBusinessException;
	List<Queue> obtenerColasDisponibles()throws PurecloudBusinessException;
}
