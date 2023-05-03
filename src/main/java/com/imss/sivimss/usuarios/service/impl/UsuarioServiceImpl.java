package com.imss.sivimss.usuarios.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;

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
import com.imss.sivimss.usuarios.beans.BusquedaDto;
import com.imss.sivimss.usuarios.beans.Usuario;
import com.imss.sivimss.usuarios.exception.BadRequestException;
import com.imss.sivimss.usuarios.model.request.UsuarioDto;
import com.imss.sivimss.usuarios.model.request.UsuarioRequest;
import com.imss.sivimss.usuarios.model.response.RolResponse;
import com.imss.sivimss.usuarios.service.UsuarioService;
import com.imss.sivimss.usuarios.util.AppConstantes;
import com.imss.sivimss.usuarios.util.ConvertirGenerico;
import com.imss.sivimss.usuarios.util.DatosRequest;
import com.imss.sivimss.usuarios.util.LogUtil;
import com.imss.sivimss.usuarios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.usuarios.util.Response;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Value("${endpoints.generico-paginado}")
	private String urlGenericoPaginado;
	
	@Value("${endpoints.generico-consulta}")
	private String urlGenericoConsulta;
	
	@Value("${endpoints.generico-crear}")
	private String urlGenericoCrear;
	
	@Value("${endpoints.generico-actualizar}")
	private String urlGenericoActualizar;
	
	@Value("${endpoints.generico-reportes}")
	private String urlReportes;
	
	private static final String nombrePdfReportes = "reportes/generales/ReporteCatUsuarios.jrxml";
	
	private static final String infoNoEncontrada = "No se encontró información relacionada a tu búsqueda.";
	
	private static final String ALTA = "alta";
	private static final String BAJA = "baja";
	private static final String MODIFICACION = "modificacion";
	private static final String CONSULTA = "consulta";
	
	@Autowired 
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LogUtil logUtil;

	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Override
	public Response<?> consultarUsuarios(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario = new Usuario();
		Gson gson = new Gson();

		String datosJson = String.valueOf(authentication.getPrincipal());
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		
		return providerRestTemplate.consumirServicio(usuario.obtenerUsuarios(request, busqueda).getDatos(), urlGenericoPaginado,
				authentication);
		
	}

    @Override
	public Response<?> catalogoRoles(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		List<RolResponse> rolResponses;
		Response<?> response = providerRestTemplate.consumirServicio(usuario.catalogoRoles().getDatos(), urlGenericoConsulta, 
				authentication);
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
		
		Response<?> response = providerRestTemplate.consumirServicio(usuario.buscarUsuario(request).getDatos(), urlGenericoPaginado,
				authentication);
		ArrayList datos1 = (ArrayList) ((LinkedHashMap) response.getDatos()).get("content");
		if (datos1.isEmpty()) {
			response.setMensaje(infoNoEncontrada);
	    }
		
		try {
		     return response;
		} catch (Exception e) {
			//logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return null;
		}
	}
	
	@Override
	public Response<?> validaCurp(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		return providerRestTemplate.consumirServicio(usuario.checaCurp(request).getDatos(), urlGenericoConsulta,
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
		
	    return new Response<Object>(false, HttpStatus.OK.value(), "Exito" , ConvertirGenerico.convertInstanceOfObject(usuario.consistenciaCurp()) );

		
	}
	
	@Override
	public Response<?> validaMatricula(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		return providerRestTemplate.consumirServicio(usuario.checaMatricula(request).getDatos(), urlGenericoConsulta,
				authentication);
		
	}

	@Override
	public Response<?> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario= new Usuario();
		return providerRestTemplate.consumirServicio(usuario.detalleUsuario(request).getDatos(), urlGenericoConsulta,
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
		Response<?> request1 = providerRestTemplate.consumirServicio(usuario.totalUsuarios(request).getDatos(), urlGenericoConsulta,
				authentication);
		ArrayList<LinkedHashMap> datos1 = (ArrayList) request1.getDatos();
		usuario.setClaveUsuario(generarUsuario(primerNombre, usuarioRequest.getPaterno(), datos1.get(0).get("total").toString()));
		
		CharSequence contrasena = generaContrasena(primerNombre, usuarioRequest.getPaterno());
		usuario.setPassword(passwordEncoder.encode(contrasena));
		usuario.setIdUsuarioAlta(usuarioDto.getIdUsuario());
		
		try {
		    return providerRestTemplate.consumirServicio(usuario.insertar().getDatos(), urlGenericoCrear, authentication);
		} catch (Exception e) {
			//logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), ALTA, authentication);
			return null;
		}
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
		usuario.setIdUsuarioModifica(usuarioDto.getIdUsuario());
		
		try {
		    return providerRestTemplate.consumirServicio(usuario.actualizar().getDatos(), urlGenericoActualizar, authentication);
		} catch (Exception e) {
			//logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), MODIFICACION, authentication);
			return null;
		}
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
		usuario.setIdUsuarioBaja(usuarioDto.getIdUsuario());
		try {
		    return providerRestTemplate.consumirServicio(usuario.cambiarEstatus().getDatos(), urlGenericoActualizar, authentication);
		} catch (Exception e) {
			//logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), BAJA, authentication);
			return null;
		}
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
		response =  providerRestTemplate.consumirServicio(usuario.consultaParamSiap(request).getDatos(), urlGenericoConsulta,
				authentication);
		ArrayList<LinkedHashMap> datosResp = (ArrayList<LinkedHashMap>) response.getDatos();
		if (datosResp.isEmpty()) {
			log.error("No se encuentra parámetro");
		} else if (datosResp.get(0).get("valor").toString().equals("0")) {
			log.info("No buscar en SIAP");
		} else {
			log.info("Buscar en SIAP");
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
		response = providerRestTemplate.consumirServicio(usuario.consultaParamRenapo(request).getDatos(), urlGenericoConsulta,
				authentication);
		ArrayList<LinkedHashMap> datosResp = (ArrayList<LinkedHashMap>) response.getDatos();
		if (datosResp.isEmpty()) {
			log.error("No se encuentra parámetro");
		} else
		if (datosResp.get(0).get(AppConstantes.VALOR).toString().equals("0")) {
			log.info("No buscar en RENAPO");
		} else {
			log.info("Buscar en RENAPO");
		}
		
		return response;
	}
	
	@Override
	public Response<?> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto reporteDto = gson.fromJson(datosJson, BusquedaDto.class);
		
		Map<String, Object> envioDatos = new Usuario().generarReporte(reporteDto, nombrePdfReportes);
		return providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
	}
	
}
