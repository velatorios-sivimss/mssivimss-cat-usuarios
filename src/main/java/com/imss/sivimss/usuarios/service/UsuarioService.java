package com.imss.sivimss.usuarios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.usuarios.util.DatosRequest;
import com.imss.sivimss.usuarios.util.Response;

public interface UsuarioService {

	Response<Object> consultarUsuarios(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> buscarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> catalogoRoles(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> validaCurp(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> consistCurp(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> validaMatricula(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> agregarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> actualizarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> cambiarEstatusUsuario(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> consultaSiap(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> consultaRenapo(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException;
	
}
