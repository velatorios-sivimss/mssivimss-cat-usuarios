package com.imss.sivimss.usuarios.configuration.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.imss.sivimss.usuarios.model.entity.PersonaEntity;

public interface PersonaMapper {

	@Insert(value = "INSERT INTO SVC_PERSONA (CVE_RFC, CVE_CURP, CVE_NSS, NOM_PERSONA, NOM_PRIMER_APELLIDO, NOM_SEGUNDO_APELLIDO, NUM_SEXO, REF_OTRO_SEXO, FEC_NAC, ID_PAIS, ID_ESTADO, REF_TELEFONO, REF_TELEFONO_FIJO, REF_CORREO, TIP_PERSONA, NUM_INE, ID_ESTADO_NACIMIENTO) "
			+ "VALUES ( #{in.cveRFC}, #{in.cveCURP}, #{in.cveNSS}, #{in.nombre}, #{in.paterno}, #{in.materno}, #{in.numSexo}, #{in.desOtroSexo}, #{in.fecNac}, #{in.idPais}, #{in.idEstado}, #{in.desTelefono }, #{in.desTelefonoFijo }, #{in.correo}, #{in.tipPersona}"
			+ ",#{in.numINE},#{in.idEstadoNacimiento} )")
	@Options(useGeneratedKeys = true,keyProperty = "idPersona", keyColumn="idPersona")
	public int agregarPersona(@Param("in")PersonaEntity persona);
	
	@Update(value = " UPDATE SVC_PERSONA SET REF_CORREO  = #{in.correo} , ID_USUARIO_MODIFICA = #{in.idUsuarioModifica} , FEC_ACTUALIZACION = CURRENT_TIMESTAMP() "
			+ "WHERE ID_PERSONA =  #{in.idPersona}  ")
	public int actualizarPersona(@Param("in")PersonaEntity persona);
}
