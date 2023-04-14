package com.imss.usuarios.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.imss.usuarios.util.DatosRequest;
import com.imss.usuarios.util.Response;

public interface UsuarioService {

	Response<?> consultarUsuarios(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> buscarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> catalogoRoles(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> pruebausrpass(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> validaCurp(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> consistCurp(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> validaMatricula(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> agregarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> actualizarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> cambiarEstatusUsuario(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> consultaSiap(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<?> consultaRenapo(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException;
	
}
