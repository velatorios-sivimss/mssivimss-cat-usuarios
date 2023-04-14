package com.imss.usuarios.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusquedaDto {
	
	private Integer idOficina;
	private Integer idDelegacion;
	private Integer idVelatorio;
	private Integer idRol;
	private String tipoReporte;

}
