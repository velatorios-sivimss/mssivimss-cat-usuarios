package com.imss.usuarios.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imss.usuarios.service.UsuarioService;
import com.imss.usuarios.util.DatosRequest;
import com.imss.usuarios.util.Response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	private UsuarioService usuarioService;
	
	@PostMapping("/consulta")
	public Response<?> consultaLista(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.consultarUsuarios(request,authentication);
      
	}
	
	@PostMapping("/roles")
	public Response<?> catalogoRoles(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.catalogoRoles(request,authentication);
      
	}
	
	@PostMapping("/buscar")
	public Response<?> buscar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.buscarUsuario(request,authentication);
      
	}
	
	@PostMapping("/valida-curp")
	public Response<?> validaCurp(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.validaCurp(request,authentication);
      
	}

	@PostMapping("/valida-matricula")
	public Response<?> validaMatricula(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.validaMatricula(request,authentication);
      
	}
	
	@PostMapping("/detalle")
	public Response<?> detalle(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.detalleUsuario(request,authentication);
      
	}
	
	@PostMapping("/prbusrpass")
	public Response<?> pruebausrpass(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.pruebausrpass(request,authentication);
      
	}
	
	@PostMapping("/agregar")
	public Response<?> agregar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.agregarUsuario(request,authentication);
      
	}
	
	@PostMapping("/actualizar")
	public Response<?> actualizar(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.actualizarUsuario(request,authentication);
      
	}
	
	@PostMapping("/cambiar-estatus")
	public Response<?> cambiarEstatus(@RequestBody DatosRequest request,Authentication authentication) throws IOException {
	
		return usuarioService.cambiarEstatusUsuario(request,authentication);
      
	}
	
}
