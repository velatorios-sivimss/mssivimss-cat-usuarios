package com.imss.sivimss.usuarios.model.entity;


import lombok.Data;

@Data
public class PersonaEntity {
	private Integer idPersona;
	private String cveCURP;
	private String cveRFC;
	private String cveNSS;
	private String nombre;
	private String paterno;
	private String materno;
	private Integer numSexo;
	private String desOtroSexo;
	private String fecNac;
	private Integer idPais;
	private Integer idEstado;
	private String desTelefono ;
	private String desTelefonoFijo ;
	private String correo;
	private String tipPersona;
	private String numINE;
	private Integer idUsuarioAlta;
	private Integer idUsuarioModifica;
	private Integer idUsuarioBaja;
	private String fecAlta;
	private String fecActualizacion;
	private String fecBaja;
}
