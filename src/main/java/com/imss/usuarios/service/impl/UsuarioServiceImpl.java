package com.imss.usuarios.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.imss.usuarios.util.Response;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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
	
	@SuppressWarnings("unchecked")
	@Override
	public Response<?> consistCurp(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		usuario.setNombre(usuarioRequest.getNombre().toUpperCase());
		usuario.setPaterno(usuarioRequest.getPaterno().toUpperCase());
		usuario.setMaterno(usuarioRequest.getMaterno().toUpperCase());
		
		return new Response<Object>(false, HttpStatus.OK.value(), "Exito" , ConvertirGenerico.convertInstanceOfObject(usuario.consistenciaCurp(request)) );
		
	}
	
	@Override
	public Response<?> validaMatricula(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		return providerRestTemplate.consumirServicio(usuario.checaMatricula(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
		
	}

	@Override
	public Response<?> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		return providerRestTemplate.consumirServicio(usuario.detalleUsuario(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
	}
	
	@Override
	public Response<?> pruebausrpass(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		// Prueba
		Response<?> request1 = providerRestTemplate.consumirServicio(usuario.totalUsuarios(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
		ArrayList<LinkedHashMap> datos1 = (ArrayList) request1.getDatos();
		String nombre = "Pedro Antonio";
		String paterno = "Sanchez";
		Integer espacio = nombre.indexOf(' ');
		String primerNombre = (espacio == -1) ? nombre : nombre.substring(0, espacio);
		String cveUsuario = generarUsuario(primerNombre, paterno, datos1.get(0).get("total").toString());
		String contrasena = generaContrasena(primerNombre, paterno);
		// Fin Prueba 
		return providerRestTemplate.consumirServicio(datos1.get(0), urlDominioConsulta + "/generico/consulta",
				authentication);
	}


	@Override
	public Response<?> agregarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		
		Integer espacio = usuarioRequest.getNombre().indexOf(' ');
		String primerNombre = (espacio == -1) ? usuarioRequest.getNombre() : usuarioRequest.getNombre().substring(0, espacio);
		Response<?> request1 = providerRestTemplate.consumirServicio(usuario.totalUsuarios(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
		ArrayList<LinkedHashMap> datos1 = (ArrayList) request1.getDatos();
		usuario.setClaveUsuario(generarUsuario(primerNombre, usuarioRequest.getPaterno(), datos1.get(0).get("total").toString()));
		
		CharSequence contrasena = generaContrasena(primerNombre, usuarioRequest.getPaterno());
		usuario.setPassword(passwordEncoder.encode(contrasena));
		usuario.setIdUsuarioAlta(usuarioDto.getId());
		
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
		usuario.setIdUsuarioModifica(usuarioDto.getId());
		
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
		usuario.setIdUsuarioBaja(usuarioDto.getId());
		return providerRestTemplate.consumirServicio(usuario.cambiarEstatus().getDatos(), urlDominioConsulta + "/generico/actualizar",
				authentication);
	}
	
	private String generarUsuario(String primerNombre, String paterno, String consecutivo) {
		
		return primerNombre.concat(paterno.substring(0, 1)).concat(String.format("%03d", Integer.parseInt(consecutivo) + 1));
	}

	private String generaContrasena(String primerNombre, String paterno) {
		char[] caracterEsp = {'#','$','^','+','=','!','*','(',')','@','%','&'};
		String mes = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1);

		return primerNombre.concat(String.valueOf(caracterEsp[new Random().nextInt(11)])).concat(".").
				concat(paterno.substring(0, 2)).concat(mes);
	}

	@Override
	public Response<?> consultaSiap(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		Response<?> response;

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		response =  providerRestTemplate.consumirServicio(usuario.consultaParamSiap(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
		ArrayList<LinkedHashMap> datosResp = (ArrayList<LinkedHashMap>) response.getDatos();
		if (datosResp.get(0).get("valor").toString().equals("0")) {
			System.out.println("No buscar en SIAP");
		} else {
			System.out.println("Buscar en SIAP");
		}
			
		
		return response;
	}

	@Override
	public Response<?> consultaRenapo(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		Response<?> response;

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		response = providerRestTemplate.consumirServicio(usuario.consultaParamRenapo(request).getDatos(), urlDominioConsulta + "/generico/consulta",
				authentication);
		ArrayList<LinkedHashMap> datosResp = (ArrayList<LinkedHashMap>) response.getDatos();
		if (datosResp.get(0).get("valor").toString().equals("0")) {
			System.out.println("No buscar en RENAPO");
		} else {
			System.out.println("Buscar en RENAPO");
		}
		
		return response;
	}
	
}
