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
	private Integer estatus;
	private Integer idEdoNacimiento;
	
}
