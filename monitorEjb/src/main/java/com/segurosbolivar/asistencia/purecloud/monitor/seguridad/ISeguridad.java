package com.segurosbolivar.asistencia.purecloud.monitor.seguridad;

import java.io.Serializable;

import javax.ejb.Local;

import com.segurosbolivar.asistencia.purecloud.monitor.excepciones.PurecloudBusinessException;

@Local
public interface ISeguridad extends Serializable {

	String obtenerToken()throws PurecloudBusinessException;

}
