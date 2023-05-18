package com.imss.sivimss.usuarios.beans;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;

import com.imss.sivimss.usuarios.model.request.UsuarioRequest;
import com.imss.sivimss.usuarios.util.AppConstantes;
import com.imss.sivimss.usuarios.util.DatosRequest;
import com.imss.sivimss.usuarios.util.QueryHelper;

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
	
	private static final String formatoFecha =  "DATE_FORMAT(FEC_NACIMIENTO,'%d/%m/%Y')";

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
	public static final String IND_ACTIVO = "IND_ACTIVO";
	
	public DatosRequest catalogoRoles(DatosRequest request) {
		String idNivel = request.getDatos().get("id").toString();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT ID_ROL, DES_ROL FROM SVC_ROL WHERE ID_OFICINA = " + idNivel;
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
		q.agregarParametroValues(IND_ACTIVO, "1");
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
		q.agregarParametroValues(IND_ACTIVO, "" + this.getEstatus() + "");
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
		String query = "UPDATE SVT_USUARIOS SET IND_ACTIVO=!IND_ACTIVO , FEC_BAJA=CURRENT_TIMESTAMP(), ID_USUARIO_BAJA='"
				+ this.idUsuarioBaja + "' WHERE ID_USUARIO = " + this.id + ";";
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest obtenerUsuarios(DatosRequest request, BusquedaDto busqueda) {
		
		StringBuilder query = new StringBuilder("SELECT ID_USUARIO AS id, DES_CURP AS curp, CVE_MATRICULA AS matricula, ");
		query.append(" NOM_USUARIO AS nombre, NOM_APELLIDO_PATERNO AS paterno, NOM_APELLIDO_MATERNO AS materno, ");
	    query.append(formatoFecha + " AS fecNacimiento, DES_CORREOE AS correo, ID_OFICINA AS idOficina, ");
		query.append(" ID_DELEGACION AS idDelegacion, ID_VELATORIO AS idVelatorio, ID_ROL AS idRol, IND_ACTIVO AS estatus, CVE_USUARIO AS usuario ");
		query.append(" FROM SVT_USUARIOS ");
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
		
		StringBuilder query = new StringBuilder("SELECT ID_USUARIO AS id, DES_CURP AS curp, CVE_MATRICULA AS matricula, ");
		query.append(" NOM_USUARIO AS nombre, NOM_APELLIDO_PATERNO AS paterno, NOM_APELLIDO_MATERNO AS materno, ");
	    query.append(formatoFecha + " AS fecNacimiento, DES_CORREOE AS correo, ID_OFICINA AS idOficina, ");
		query.append(" ID_DELEGACION AS idDelegacion, ID_VELATORIO AS idVelatorio, ID_ROL AS idRol, IND_ACTIVO AS estatus, CVE_USUARIO AS usuario ");
		query.append(" FROM SVT_USUARIOS ");
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
				+ formatoFecha + " AS fecNacimiento, u.DES_CORREOE AS correo, DES_NIVELOFICINA AS oficina, DES_DELEGACION AS delegacion, "
				+ " NOM_VELATORIO AS velatorio, r.DES_ROL AS rol, u.IND_ACTIVO AS estatus, u.CVE_USUARIO AS usuario FROM SVT_USUARIOS u ");
		query.append(" LEFT JOIN SVC_ROL r USING (ID_ROL) ");
		query.append(" LEFT JOIN SVC_NIVEL_OFICINA o ON o.ID_OFICINA = u.ID_OFICINA ");
		query.append(" LEFT JOIN SVC_DELEGACION d ON d.ID_DELEGACION = u.ID_DELEGACION ");
		query.append(" LEFT JOIN SVC_VELATORIO v ON v.ID_VELATORIO = u.ID_VELATORIO " );
		query.append(" WHERE u.ID_USUARIO = " + Integer.parseInt(idUsuario));
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public DatosRequest checaCurp(DatosRequest request) {
		String query = "SELECT COUNT(*) AS valor FROM SVT_USUARIOS WHERE DES_CURP = '" + this.curp + "'";
		
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
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	
	public Boolean consistenciaCurp() {
		Boolean valido = true;
	    this.nombre = this.nombre.toUpperCase().replace("JOSE ", "");
	    this.nombre = this.nombre.toUpperCase().replace("MARIA ", "");

		if (!this.paterno.substring(0, 2).equals(this.curp.substring(0, 2)) || this.materno.charAt(0) != this.curp.charAt(2)) {
			valido = false;
		} else if (this.nombre.charAt(0) != this.curp.charAt(3) || !this.fecNacimiento.substring(2, 8).equals(this.curp.substring(4, 10))) {
			valido = false;
		} else if (!paterno.contains(this.curp.substring(13, 14)) || !materno.contains(this.curp.substring(14, 15)) || !nombre.contains(this.curp.substring(15, 16))) {
			valido = false;
		}
		
		return valido;
	}

	public DatosRequest consultaParamSiap(DatosRequest request) {
		String query = "SELECT IF(IND_ACTIVO,1,0) AS valor FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO = 'Validacion SIAP'";
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		
		return request;
	}
	
	public DatosRequest consultaParamRenapo(DatosRequest request) {
		String query = "SELECT IF(IND_ACTIVO,1,0) AS valor FROM SVC_PARAMETRO_SISTEMA WHERE DES_PARAMETRO = 'Validacion RENAPO'";
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}
	
	public Map<String, Object> generarReporte(BusquedaDto reporteDto,String nombrePdfReportes){
		Map<String, Object> envioDatos = new HashMap<>();
		StringBuilder condicion = new StringBuilder("");
		if (reporteDto.getIdOficina() != null) {
			condicion.append(" AND ID_OFICINA = ").append(reporteDto.getIdOficina());
		}
		if (reporteDto.getIdDelegacion() != null) {
			condicion.append(" AND ID_DELEGACION = ").append(reporteDto.getIdDelegacion());
		}
		if (reporteDto.getIdVelatorio() != null) {
			condicion.append(" AND ID_VELATORIO = ").append(reporteDto.getIdVelatorio());
		}
		if (reporteDto.getIdRol() != null) {
			condicion.append(" AND ID_ROL = ").append(this.getIdRol());
		}
		envioDatos.put("condicion", condicion.toString());
		envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);
		
		return envioDatos;
	}
	
}
