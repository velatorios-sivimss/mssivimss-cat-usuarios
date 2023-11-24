package com.imss.sivimss.usuarios.configuration.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.imss.sivimss.usuarios.model.entity.UsuarioEntity;

public interface UsuarioMapper {
	
	@Insert(value = "INSERT INTO SVT_USUARIOS (CVE_MATRICULA, ID_OFICINA, ID_DELEGACION, ID_VELATORIO"
			+ ", ID_ROL, IND_ACTIVO, CVE_USUARIO, CVE_CONTRASENIA, ID_PERSONA, IND_CONTRATANTE, ID_USUARIO_ALTA) "
			+ "VALUES (#{in.matricula}, #{in.idOficina}, #{in.idDelegacion}, #{in.idVelatorio}"
			+ ", #{in.idRol}, #{in.indActivo}, #{in.cveUsuario}, #{in.cveContrasenia}, #{in.idPersona}, #{in.indContratante}, #{in.idUsuarioAlta})")
	@Options(useGeneratedKeys = true, keyProperty = "idUsuario", keyColumn = "idUsuario")
	public int agregarNuevoUsuario(@Param("in") UsuarioEntity usuario);

	@Select("SELECT su.ID_USUARIO AS id, su.CVE_MATRICULA AS matricula, sp.CVE_CURP AS curp, sp.NOM_PERSONA AS nombre, sp.NOM_PRIMER_APELLIDO AS paterno"
			+ ", sp.NOM_SEGUNDO_APELLIDO AS materno, DATE_FORMAT(sp.FEC_NAC , '%d/%m/%Y') AS fecNacimiento, sp.ID_ESTADO AS idEdoNacimiento, sp.REF_CORREO AS correo"
			+ ", su.ID_OFICINA AS idOficina, sno.DES_NIVELOFICINA AS desOficina, su.ID_DELEGACION AS idDelegacion, su.ID_VELATORIO  AS idVelatorio, su.ID_ROL AS idRol"
			+ ", sr.DES_ROL AS desRol, su.IND_ACTIVO AS estatus, su.CVE_USUARIO AS usuario" + " FROM SVT_USUARIOS su "
			+ " JOIN SVC_PERSONA sp ON sp.ID_PERSONA = su.ID_PERSONA "
			+ " JOIN SVC_NIVEL_OFICINA sno ON sno.ID_OFICINA = su.ID_OFICINA "
			+ " JOIN SVC_ROL sr ON sr.ID_ROL = su.ID_ROL ")
	public List<Map<String, Object>> consultaUsuarios();

	@Select("SELECT su.ID_USUARIO AS id, su.CVE_MATRICULA AS claveMatricula, sp.CVE_CURP AS curp, sp.NOM_PERSONA AS nombre, sp.NOM_PRIMER_APELLIDO AS paterno"
			+ ", sp.NOM_SEGUNDO_APELLIDO AS materno, DATE_FORMAT(sp.FEC_NAC , '%d/%m/%Y') AS fecNacimiento, sp.ID_ESTADO AS idEdoNacimiento, sp.REF_CORREO AS correo"
			+ ", su.ID_OFICINA AS idOficina, sno.DES_NIVELOFICINA AS desOficina, su.ID_DELEGACION AS idDelegacion, su.ID_VELATORIO  AS idVelatorio, su.ID_ROL AS idRol"
			+ ", sr.DES_ROL AS desRol, su.IND_ACTIVO AS estatus, su.CVE_USUARIO AS usuario" + " FROM SVT_USUARIOS su "
			+ " JOIN SVC_PERSONA sp ON sp.ID_PERSONA = su.ID_PERSONA "
			+ " JOIN SVC_NIVEL_OFICINA sno ON sno.ID_OFICINA = su.ID_OFICINA "
			+ " JOIN SVC_ROL sr ON sr.ID_ROL = su.ID_ROL " + " ${where}")
	public List<Map<String, Object>> buscarUsuarios(@Param("where") String where);

	@Select("SELECT su.ID_USUARIO AS id, sp.CVE_CURP AS curp, su.CVE_MATRICULA AS matricula, sp.NOM_PERSONA  AS nombre, sp.NOM_PRIMER_APELLIDO  AS paterno, sp.NOM_SEGUNDO_APELLIDO  AS materno,"
			+ " DATE_FORMAT(sp.FEC_NAC, '%d/%m/%Y') AS fecNacimiento, sp.ID_ESTADO_NACIMIENTO AS idEdoNacimiento, se.DES_ESTADO AS desEdoNacimiento, sp.REF_CORREO AS correo, su.ID_OFICINA AS idOficina,"
			+ " sno.DES_NIVELOFICINA AS oficina, su.ID_DELEGACION AS idDelegacion, sd.DES_DELEGACION AS delegacion, su.ID_VELATORIO AS idVelatorio, sv.DES_VELATORIO AS velatorio, su.ID_ROL AS idRol,"
			+ " sr.DES_ROL AS rol, su.IND_ACTIVO AS estatus, su.CVE_USUARIO AS usuario, 'XXXXXXXXXXXXX' AS contrasenia"
			+ " FROM SVT_USUARIOS su " + " JOIN SVC_PERSONA sp ON sp.ID_PERSONA = su.ID_PERSONA "
			+ " JOIN SVC_ROL sr ON su.ID_ROL = sr.ID_ROL "
			+ " JOIN SVC_DELEGACION sd ON sd.ID_DELEGACION = su.ID_DELEGACION "
			+ " JOIN SVC_VELATORIO sv ON sv.ID_VELATORIO = su.ID_VELATORIO "
			+ " JOIN SVC_NIVEL_OFICINA sno ON sno.ID_OFICINA = su.ID_OFICINA "
			+ " LEFT JOIN SVC_ESTADO se ON se.ID_ESTADO = sp.ID_ESTADO_NACIMIENTO " 
			+ " ${where}")
	public List<Map<String, Object>> detalleUsuario(@Param("where") String where);

	@Select("SELECT COUNT(*) AS valor FROM SVT_USUARIOS su  JOIN SVC_PERSONA sp ON sp.ID_PERSONA = su.ID_PERSONA WHERE sp.CVE_CURP = #{curp} ")
	public List<Map<String, Object>> validaCurp(@Param("curp") String curp);

	@Select("SELECT sr.ID_ROL AS id, sr.DES_ROL nombre FROM SVC_ROL sr WHERE sr.IND_ACTIVO = 1 AND sr.ID_OFICINA = #{idOficina}")
	public List<Map<String, Object>> catalogoRoles(@Param("idOficina") String idOficina);

	@Select("SELECT COUNT(*) AS valor FROM SVT_USUARIOS su  WHERE su.CVE_MATRICULA = #{matricula} ")
	public List<Map<String, Object>> validaMatricula(@Param("matricula") String matricula);

	@Update(value = "UPDATE SVT_USUARIOS SET ID_OFICINA = #{in.idOficina}, ID_DELEGACION = #{in.idDelegacion} , ID_VELATORIO = #{in.idVelatorio}"
			+ ", ID_ROL = #{in.idRol}, IND_ACTIVO = #{in.indActivo}, ID_USUARIO_MODIFICA = #{in.idUsuarioMidifica} , FEC_ACTUALIZACION = CURRENT_TIMESTAMP() "
			+ " WHERE ID_USUARIO = #{in.idUsuario} ")
	public int actualizarUsuario(@Param("in") UsuarioEntity usuario);

	@Select("SELECT su.ID_PERSONA AS idPersona FROM SVT_USUARIOS su WHERE su.ID_USUARIO = #{idUsuario}")
	public int obtenerIdPersona(@Param("idUsuario") Integer idUsuario);
	
	@Update(value = "UPDATE SVT_USUARIOS SET IND_ACTIVO=!IND_ACTIVO, ID_USUARIO_MODIFICA = #{in.idUsuarioMidifica} , FEC_ACTUALIZACION = CURRENT_TIMESTAMP() "
			+ " WHERE ID_USUARIO = #{in.idUsuario} ")
	public int actualizarEstatusUsuario(@Param("in") UsuarioEntity usuario);
	
	@Select(value= "SELECT COUNT(*) AS total FROM SVT_USUARIOS")
	public int obtenerTotalUsuarios();
	
}
