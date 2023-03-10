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
	private String claveAlta;
	private String claveModifica;
	private String claveBaja;

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
		
	}

	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVT_USUARIOS");
		q.agregarParametroValues("DES_CURP", "'" + this.curp + "'");
		q.agregarParametroValues("CVE_MATRICULA", "'" + this.claveUsuario + "'");
		q.agregarParametroValues("CVE_USUARIO", "'" + this.claveUsuario + "'");
		q.agregarParametroValues("NOM_USUARIO", "'" + this.nombre + "'");
		q.agregarParametroValues("NOM_APELLIDO_PATERNO", "'" + this.paterno + "'");
		q.agregarParametroValues("NOM_APELLIDO_MATERNO", "'" + this.materno + "'");
		q.agregarParametroValues("FEC_NACIMIENTO", "NULL");
		q.agregarParametroValues("DES_CORREOE", "'" + this.correo + "'");
		q.agregarParametroValues("ID_OFICINA", "" + this.idOficina + "");
		q.agregarParametroValues("ID_DELEGACION", "" + this.idDelegacion + "");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("ID_ROL", "" + this.idRol + "");
		q.agregarParametroValues("CVE_ESTATUS", "1");
		q.agregarParametroValues("CVE_CONTRASENIA", "'" + this.password + "'");
		q.agregarParametroValues("FEC_ALTA", "NOW()");
		q.agregarParametroValues("CVE_MATRICULA_ALTA", "'" + this.claveAlta + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NULL");
		q.agregarParametroValues("FEC_BAJA", "NULL");
		q.agregarParametroValues("CVE_MATRICULA_MODIFICA", "NULL");
		q.agregarParametroValues("CVE_MATRICULA_BAJA", "NULL");
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
		q.agregarParametroValues("DES_CURP", "'" + this.curp + "'");
		q.agregarParametroValues("NOM_USUARIO", "'" + this.nombre + "'");
		q.agregarParametroValues("NOM_APELLIDO_PATERNO", "'" + this.paterno + "'");
		q.agregarParametroValues("NOM_APELLIDO_MATERNO", "'" + this.materno + "'");
		q.agregarParametroValues("DES_CORREOE", "'" + this.correo + "'");
		q.agregarParametroValues("ID_OFICINA", "" + this.idOficina + "");
		q.agregarParametroValues("ID_DELEGACION", "" + this.idDelegacion + "");
		q.agregarParametroValues("ID_VELATORIO", "" + this.idVelatorio + "");
		q.agregarParametroValues("ID_ROL", "" + this.idRol + "");
		q.agregarParametroValues("CVE_MATRICULA_MODIFICA", "'" + this.claveModifica + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
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
		String query = "UPDATE SVT_USUARIOS SET CVE_ESTATUS=!CVE_ESTATUS , FEC_BAJA=NOW(), CVE_MATRICULA_BAJA='"
				+ this.claveBaja + "' WHERE ID_USUARIO=" + this.id + ";";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest obtenerUsuarios(DatosRequest request, NivelDto nivel) {
		
		StringBuilder query = new StringBuilder("SELECT * FROM SVT_USUARIOS ");
		if (nivel.getNivel() > 1) {
			query.append(" WHERE ID_DELEGACION = ").append(nivel.getDelegacion());
			if (nivel.getNivel() == 3) {
				query.append(" AND ID_VELATORIO = ").append(nivel.getVelatorio());
			}
		}
        query.append(" ORDER BY ID_USUARIO DESC");
        
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest buscarUsuario(DatosRequest request, NivelDto nivel) {
		String palabra = request.getDatos().get("palabra").toString();
		StringBuilder query = new StringBuilder("SELECT * FROM SVT_USUARIOS ");
		query.append(" WHERE CONCAT(NOM_USUARIO,' ',NOM_APELLIDO_PATERNO,' ' ,NOM_APELLIDO_MATERNO) LIKE '%" + palabra + "%'");
		if (nivel.getNivel() > 1) {
			query.append(" AND ID_DELEGACION = ").append(nivel.getDelegacion());
			if (nivel.getNivel() == 3) {
				query.append(" AND ID_VELATORIO = ").append(nivel.getVelatorio());
			}
		}
		query.append(" ORDER BY ID_USUARIO DESC");
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("palabra");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest detalleUsuario(DatosRequest request) {
		String idUsuario = request.getDatos().get("id").toString();
		StringBuilder query = new StringBuilder("SELECT * FROM SVT_USUARIOS ");
		query.append(" WHERE ID_USUARIO = " + Integer.parseInt(idUsuario));
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

}
