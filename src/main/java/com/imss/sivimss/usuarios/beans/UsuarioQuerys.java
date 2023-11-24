package com.imss.sivimss.usuarios.beans;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.usuarios.model.request.UsuarioRequest;
import com.imss.sivimss.usuarios.util.AppConstantes;
import com.imss.sivimss.usuarios.util.DatosRequest;

@Service
public class UsuarioQuerys {
	
	public String queryConsultaUsuarios(DatosRequest request) {
		return "SELECT su.ID_USUARIO AS id, su.CVE_MATRICULA AS matricula, sp.CVE_CURP AS curp, sp.NOM_PERSONA AS nombre, sp.NOM_PRIMER_APELLIDO AS paterno"
			+ ", sp.NOM_SEGUNDO_APELLIDO AS materno, DATE_FORMAT(sp.FEC_NAC , '%d/%m/%Y') AS fecNacimiento, sp.ID_ESTADO AS idEdoNacimiento, sp.REF_CORREO AS correo"
			+ ", su.ID_OFICINA AS idOficina, sno.DES_NIVELOFICINA AS desOficina, su.ID_DELEGACION AS idDelegacion, su.ID_VELATORIO  AS idVelatorio, su.ID_ROL AS idRol"
			+ ", sr.DES_ROL AS desRol, su.IND_ACTIVO AS estatus, su.CVE_USUARIO AS usuario" + " FROM SVT_USUARIOS su "
			+ " JOIN SVC_PERSONA sp ON sp.ID_PERSONA = su.ID_PERSONA "
			+ " JOIN SVC_NIVEL_OFICINA sno ON sno.ID_OFICINA = su.ID_OFICINA "
			+ " JOIN SVC_ROL sr ON sr.ID_ROL = su.ID_ROL " + crearWhere(request) ;
	}

	private String crearWhere(DatosRequest request) {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		String where = " WHERE 1 = 1 ";
		if (usuarioRequest != null){
			if(usuarioRequest.getId() != null)
				where = where + " AND su.ID_USUARIO = " + usuarioRequest.getId();
			if (usuarioRequest.getIdOficina() != null)
				where = where + " AND su.ID_OFICINA = " + usuarioRequest.getIdOficina();
			if (usuarioRequest.getIdDelegacion() != null)
				where = where + " AND su.ID_DELEGACION = " + usuarioRequest.getIdDelegacion();
			if (usuarioRequest.getIdVelatorio() != null)
				where = where + " AND su.ID_VELATORIO =" + usuarioRequest.getIdVelatorio();
			if (usuarioRequest.getIdRol() != null)
				where = where + " AND su.ID_ROL = " + usuarioRequest.getIdRol();
		}
		return where;
	}
}
