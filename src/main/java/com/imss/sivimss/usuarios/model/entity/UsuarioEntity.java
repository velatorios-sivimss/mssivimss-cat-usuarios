package com.imss.sivimss.usuarios.model.entity;


import lombok.Data;

@Data
public class UsuarioEntity {

	private Integer idUsuario;	
	private Integer idPersona;	
	private String matricula;
	private Integer idOficina;
	private Integer idDelegacion;
	private Integer idVelatorio;
	private Integer idRol;
	private Integer indActivo;
	private String cveUsuario;
	private String cveContrasenia;
	private Integer indContratante;
	private Integer idUsuarioAlta;
	private Integer idUsuarioMidifica;
	private String curp;
	private String paterno;
	private String materno;
	private String nombre;
	private Boolean estatus;
	private String desRol;
	private String correo;
	private String fecNacimiento;
	private String usuario;
	private String id;
	private String desOficina;
	private Integer valor;
	private Integer idEstadoNacimiento;
}
