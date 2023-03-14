package com.imss.usuarios.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.usuarios.beans.Usuario;
import com.imss.usuarios.beans.BusquedaDto;
import com.imss.usuarios.exception.BadRequestException;
import com.imss.usuarios.model.request.UsuarioDto;
import com.imss.usuarios.model.request.UsuarioRequest;
import com.imss.usuarios.service.UsuarioService;
import com.imss.usuarios.model.response.RolResponse;
import com.imss.usuarios.util.AppConstantes;
import com.imss.usuarios.util.ConvertirGenerico;
import com.imss.usuarios.util.DatosRequest;
import com.imss.usuarios.util.ProviderServiceRestTemplate;
import com.imss.usuarios.util.QueryHelper;
import com.imss.usuarios.util.Response;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private ModelMapper modelMapper;

	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Override
	public Response<?> consultarUsuarios(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario = new Usuario();
		BusquedaDto busqueda = new BusquedaDto(1,1,1,1);
		
		return providerRestTemplate.consumirServicio(usuario.obtenerUsuarios(request, busqueda).getDatos(), urlDominioConsulta + "/generico/paginado",
				authentication);
		
	}

    @Override
	public Response<?> catalogoRoles(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		List<RolResponse> rolResponses;
		Response<?> response = providerRestTemplate.consumirServicio(usuario.catalogoRoles().getDatos(),
				urlDominioConsulta + "/generico/consulta", authentication);
		if (response.getCodigo() == 200) {
			rolResponses = Arrays.asList(modelMapper.map(response.getDatos(), RolResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(rolResponses));
		}
		return response;
	}
	
	@Override
	public Response<?> buscarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		
		return providerRestTemplate.consumirServicio(usuario.buscarUsuario(request).getDatos(), urlDominioConsulta + "/generico/paginado",
				authentication);
	}
	
	@Override
	public Response<?> validaCurp(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		return providerRestTemplate.consumirServicio(usuario.checaCurp(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
		
	}

	@Override
	public Response<?> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		String cveUsuario = generarUsuario("Pedro Antonio","Sanchez");
		System.out.println(cveUsuario);
		return providerRestTemplate.consumirServicio(usuario.detalleUsuario(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
	}


	@Override
	public Response<?> agregarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario= new Usuario(usuarioRequest);
		usuario.setClaveAlta(usuarioDto.getCorreo());
		
		return providerRestTemplate.consumirServicio(usuario.insertar().getDatos(), urlDominioConsulta + "/generico/crear",
				authentication);
	}

	@Override
	public Response<?> actualizarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		if (usuarioRequest.getId() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Usuario usuario= new Usuario(usuarioRequest);
		usuario.setClaveModifica(usuarioDto.getCorreo());
		
		return providerRestTemplate.consumirServicio(usuario.actualizar().getDatos(), urlDominioConsulta + "/generico/actualizar",
				authentication);
	}

	@Override
	public Response<?> cambiarEstatusUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		if (usuarioRequest.getId() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Usuario usuario= new Usuario(usuarioRequest);
		usuario.setClaveBaja(usuarioDto.getCorreo());
		return providerRestTemplate.consumirServicio(usuario.cambiarEstatus().getDatos(), urlDominioConsulta + "/generico/actualizar",
				authentication);
	}
	
	private String generarUsuario(String nombre, String paterno) {
		Integer espacio = nombre.indexOf(' ');
		String primerNombre = (espacio == -1) ? nombre : nombre.substring(0, espacio);
		String cveUsuario = primerNombre.concat(paterno.substring(0, 1)).concat("001");
		
		return cveUsuario;
	}

	
}
