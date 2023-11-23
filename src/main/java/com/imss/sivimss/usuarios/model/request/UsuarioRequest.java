package com.imss.sivimss.usuarios.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

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
@JsonIgnoreType(value = true)
public class UsuarioRequest {
	/*
	 * Datos de Usuario
	 * 
	 */
	private Integer id;
	private String claveMatricula;
	private Integer idOficina;
	private Integer idDelegacion;
	private Integer idVelatorio;
	private Integer idRol;
	private Integer indActivo;
	private String cveUsuario;
	private String cveContasenia;
	private Integer idUsuarioAlta;
	private Integer idUsuarioModifica;
	private Integer idUsuarioBaja;
	private String fecAlta;
	private String fecActualizacion;
	private String fecBaja;
	/*
	 * Datos de Persona
	 * 
	 */
	private String rfc;
	private String curp;
	private String nss;
	private String nombre;
	private String paterno;
	private String materno;
	private String fecNacimiento;
	private String correo;
	private String claveUsuario;
	private String password;
	private Integer estatus;
	private Integer idEdoNacimiento;
	private Integer numsexo;
	private String desOtroSexo;
	private String fecNac;
	private Integer idPais;
	private Integer idEstado;
	private String desTelefono ;
	private String desTelefonoFijo ;
	private String tipPersona;
	private String numINE;
	
}
