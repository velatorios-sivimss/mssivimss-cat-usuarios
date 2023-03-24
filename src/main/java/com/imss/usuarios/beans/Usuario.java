package com.imss.usuarios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.usuarios.model.request.UsuarioRequest;
import com.imss.usuarios.util.AppConstantes;
import com.imss.usuarios.util.DatosRequest;
import com.imss.usuarios.util.QueryHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Usuario {
	
	private Integer id;
	private String curp;
	private String claveMatricula;
    private String nombre;
	private String paterno;
	private String materno;
	private String fecNacimiento;
	private String correo;
	private Integer idOficina;
	private Integer idDelegacion;
	private Integer idVelatorio;
	private Integer idRol;
	private String claveUsuario;
	private String password;
	private Integer idUsuarioAlta;
	private Integer idUsuarioModifica;
	private Integer idUsuarioBaja;
	private Integer estatus;

	public Usuario(UsuarioRequest usuarioRequest) {
		this.id = usuarioRequest.getId();
		this.curp = usuarioRequest.getCurp();
		this.claveMatricula = usuarioRequest.getClaveMatricula();
		this.nombre = usuarioRequest.getNombre();
		this.paterno = usuarioRequest.getPaterno();
		this.materno = usuarioRequest.getMaterno();
		this.fecNacimiento = usuarioRequest.getFecNacimiento();
		this.correo = usuarioRequest.getCorreo();
		this.idOficina = usuarioRequest.getIdOficina();
		this.idDelegacion = usuarioRequest.getIdDelegacion();
		this.idVelatorio = usuarioRequest.getIdVelatorio();
		this.idRol = usuarioRequest.getIdRol();
		this.claveUsuario = usuarioRequest.getClaveUsuario();
		this.password = usuarioRequest.getPassword();
		this.estatus = usuarioRequest.getEstatus();
		
	}
	
	public static final String DES_CORREOE = "DES_CORREOE";
	public static final String ID_OFICINA = "ID_OFICINA";
	public static final String ID_DELEGACION = "ID_DELEGACION";
	public static final String ID_VELATORIO = "ID_VELATORIO";
	public static final String ID_ROL = "ID_ROL";
	public static final String CVE_ESTATUS = "CVE_ESTATUS";
	
	public DatosRequest catalogoRoles() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_ROL";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVT_USUARIOS");
		q.agregarParametroValues("DES_CURP", "'" + this.curp + "'");
		q.agregarParametroValues("CVE_MATRICULA", "'" + this.claveMatricula + "'");
		q.agregarParametroValues("NOM_USUARIO", "'" + this.nombre + "'");
		q.agregarParametroValues("NOM_APELLIDO_PATERNO", "'" + this.paterno + "'");
		q.agregarParametroValues("NOM_APELLIDO_MATERNO", "'" + this.materno + "'");
		q.agregarParametroValues("FEC_NACIMIENTO", "'" + this.fecNacimiento + "'");
		q.agregarParametroValues(DES_CORREOE, "'" + this.correo + "'");
		q.agregarParametroValues(ID_OFICINA, "" + this.idOficina + "");
		q.agregarParametroValues(ID_DELEGACION, "" + this.idDelegacion + "");
		q.agregarParametroValues(ID_VELATORIO, "" + this.idVelatorio + "");
		q.agregarParametroValues(ID_ROL, "" + this.idRol + "");
		q.agregarParametroValues(CVE_ESTATUS, "1");
		q.agregarParametroValues("CVE_USUARIO", "'" + this.claveUsuario + "'");
		q.agregarParametroValues("CVE_CONTRASENIA", "'" + this.password + "'");
		q.agregarParametroValues("FEC_ALTA", "CURRENT_TIMESTAMP()");
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.idUsuarioAlta + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NULL");
		q.agregarParametroValues("FEC_BAJA", "NULL");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", "NULL");
		q.agregarParametroValues("ID_USUARIO_BAJA", "NULL");
		String query = q.obtenerQueryInsertar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);

		return request;
	}

	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("UPDATE SVT_USUARIOS");
		q.agregarParametroValues(DES_CORREOE, "'" + this.correo + "'");
		q.agregarParametroValues(ID_OFICINA, "" + this.idOficina + "");
		q.agregarParametroValues(ID_DELEGACION, "" + this.idDelegacion + "");
		q.agregarParametroValues(ID_VELATORIO, "" + this.idVelatorio + "");
		q.agregarParametroValues(ID_ROL, "" + this.idRol + "");
		q.agregarParametroValues(CVE_ESTATUS, "" + this.getEstatus() + "");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", "'" + this.idUsuarioModifica + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "CURRENT_TIMESTAMP()");
		q.addWhere("ID_USUARIO = " + this.id);
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest cambiarEstatus() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "UPDATE SVT_USUARIOS SET CVE_ESTATUS=!CVE_ESTATUS , FEC_BAJA=CURRENT_TIMESTAMP(), ID_USUARIO_BAJA='"
				+ this.idUsuarioBaja + "' WHERE ID_USUARIO = " + this.id + ";";
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest obtenerUsuarios(DatosRequest request, BusquedaDto busqueda) {
		
		StringBuilder query = new StringBuilder("SELECT ID_USUARIO AS id, DES_CURP AS curp, CVE_MATRICULA AS matricula, "
				+ " NOM_USUARIO AS nombre, NOM_APELLIDO_PATERNO AS paterno, NOM_APELLIDO_MATERNO AS materno, "
				+ " FEC_NACIMIENTO AS fecNacimiento, DES_CORREOE AS correo, ID_OFICINA AS idOficina, ID_DELEGACION AS idDelegacion, "
				+ " ID_VELATORIO AS idVelatorio, ID_ROL AS idRol, CVE_ESTATUS AS estatus, CVE_USUARIO AS usuario FROM SVT_USUARIOS ");
		if (busqueda.getIdOficina() > 1) {
			query.append(" WHERE ID_DELEGACION = ").append(busqueda.getIdDelegacion());
			if (busqueda.getIdOficina() == 3) {
				query.append(" AND ID_VELATORIO = ").append(busqueda.getIdVelatorio());
			}
		}
        query.append(" ORDER BY ID_USUARIO DESC");
        
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest buscarUsuario(DatosRequest request) {
		
		StringBuilder query = new StringBuilder("SELECT ID_USUARIO AS id, DES_CURP AS curp, CVE_MATRICULA AS matricula, "
				+ " NOM_USUARIO AS nombre, NOM_APELLIDO_PATERNO AS paterno, NOM_APELLIDO_MATERNO AS materno, "
				+ " FEC_NACIMIENTO AS fecNacimiento, DES_CORREOE AS correo, ID_OFICINA AS idOficina, ID_DELEGACION AS idDelegacion, "
				+ " ID_VELATORIO AS idVelatorio, ID_ROL AS idRol, CVE_ESTATUS AS estatus, CVE_USUARIO AS usuario FROM SVT_USUARIOS ");
		query.append(" WHERE 1 = 1" );
		if (this.getIdOficina() != null) {
			query.append(" AND ID_OFICINA = ").append(this.getIdOficina());
		}
		if (this.getIdDelegacion() != null) {
			query.append(" AND ID_DELEGACION = ").append(this.getIdDelegacion());
		}
		if (this.getIdVelatorio() != null) {
			query.append(" AND ID_VELATORIO = ").append(this.getIdVelatorio());
		}
		if (this.getIdRol() != null) {
			query.append(" AND ID_ROL = ").append(this.getIdRol());
		}
		
		query.append(" ORDER BY ID_USUARIO DESC");
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest detalleUsuario(DatosRequest request) {
		String idUsuario = request.getDatos().get("id").toString();
		StringBuilder query = new StringBuilder("SELECT u.ID_USUARIO AS id, u.DES_CURP AS curp, u.CVE_MATRICULA AS matricula, "
				+ " u.NOM_USUARIO AS nombre, u.NOM_APELLIDO_PATERNO AS paterno, u.NOM_APELLIDO_MATERNO AS materno, "
				+ " u.FEC_NACIMIENTO AS fecNacimiento, u.DES_CORREOE AS correo, DES_NIVELOFICINA AS oficina, DES_DELEGACION AS delegacion, "
				+ " NOM_VELATORIO AS velatorio, r.DES_ROL AS rol, u.CVE_ESTATUS AS estatus, u.CVE_USUARIO AS usuario FROM SVT_USUARIOS u ");
		query.append(" LEFT JOIN svc_rol r USING (ID_ROL) ");
		query.append(" LEFT JOIN svc_nivel_oficina o ON o.ID_OFICINA = u.ID_OFICINA ");
		query.append(" LEFT JOIN svc_delegacion d ON d.ID_DELEGACION = u.ID_DELEGACION ");
		query.append(" LEFT JOIN svc_velatorio v ON v.ID_VELATORIO = u.ID_VELATORIO " );
		query.append(" WHERE u.ID_USUARIO = " + Integer.parseInt(idUsuario));
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest checaCurp(DatosRequest request) {
		String query = null;
		if (!this.curp.matches("[A-Z]{4}\\d{6}[HM][A-Z]{2}[B-DF-HJ-NP-TV-Z]{3}[A-Z0-9][0-9]")) {
			query = "SELECT 2 AS valor FROM DUAL";
	    } else {
	    	query = "SELECT COUNT(*) AS valor FROM SVT_USUARIOS WHERE DES_CURP = '" + this.curp + "'";
	    }
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest checaMatricula(DatosRequest request) {
		String query = "SELECT COUNT(*) AS valor FROM SVT_USUARIOS WHERE CVE_MATRICULA = " + this.claveMatricula;
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest totalUsuarios(DatosRequest request) {
		String query = "SELECT COUNT(*) AS total FROM SVT_USUARIOS";
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public Boolean consistenciaCurp(DatosRequest request) {
		Boolean valido = true;
	    this.nombre = this.nombre.replace("JOSE ", "");
	    this.nombre = this.nombre.replace("MARIA ", "");

		if (!this.paterno.substring(0, 2).equals(this.curp.substring(0, 2))) {
			valido = false;
		} else if (this.materno.charAt(0) != this.curp.charAt(2)) {
			valido = false;
		} else if (this.nombre.charAt(0) != this.curp.charAt(3)) {
			valido = false;
		} else if (!this.fecNacimiento.substring(2, 8).equals(this.curp.substring(4, 10))) {
			valido = false;
		} else if (!paterno.contains(this.curp.substring(13, 14))) {
			valido = false;
		} else if (!materno.contains(this.curp.substring(14, 15))) {
			valido = false;
		} else if (!nombre.contains(this.curp.substring(15, 16))) {
			valido = false;
		}
		
		return valido;
	}

	public DatosRequest consultaParamSiap(DatosRequest request) {
		String query = "SELECT IF(CVE_ESTATUS,1,0) AS valor FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO = 'Validacion SIAP'";
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		
		return request;
	}
	
	public DatosRequest consultaParamRenapo(DatosRequest request) {
		String query = "SELECT IF(CVE_ESTATUS,1,0) AS valor FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO = 'Validacion RENAPO'";
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}
	
}
