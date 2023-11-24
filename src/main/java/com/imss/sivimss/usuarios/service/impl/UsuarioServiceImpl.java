package com.imss.sivimss.usuarios.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.security.SecureRandom;
import java.util.logging.Level;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.usuarios.beans.Usuario;
import com.imss.sivimss.usuarios.beans.UsuarioQuerys;
import com.imss.sivimss.usuarios.configuration.MyBatisConfig;
import com.imss.sivimss.usuarios.configuration.mapper.PersonaMapper;
import com.imss.sivimss.usuarios.configuration.mapper.UsuarioMapper;
import com.imss.sivimss.usuarios.model.request.BusquedaDto;
import com.imss.sivimss.usuarios.model.request.UsuarioDto;
import com.imss.sivimss.usuarios.model.request.UsuarioRequest;
import com.imss.sivimss.usuarios.model.entity.UsuarioEntity;
import com.imss.sivimss.usuarios.model.entity.PersonaEntity;
import com.imss.sivimss.usuarios.service.UsuarioService;
import com.imss.sivimss.usuarios.util.AppConstantes;
import com.imss.sivimss.usuarios.util.ConvertirGenerico;
import com.imss.sivimss.usuarios.util.DatosRequest;
import com.imss.sivimss.usuarios.util.LogUtil;
import com.imss.sivimss.usuarios.util.PaginadoUtil;
import com.imss.sivimss.usuarios.util.ProviderServiceRestTemplate;
import com.imss.sivimss.usuarios.util.Response;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Value("${endpoints.dominio}")
	private String urlDominioGenerico;

	private static final String PAGINADO = "/paginado";

	private static final String CONSULTA = "/consulta";


	@Value("${endpoints.generico-reportes}")
	private String urlReportes;

	private static final String NOMBRE_PDF_REPORTES = "reportes/generales/ReporteCatUsuarios.jrxml";

	private static final String INFONOENCONTRADA = "45";


	@Autowired
	private UsuarioQuerys usuarioQuerys ;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LogUtil logUtil;

	private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);

	@Override
	public Response<Object> consultarUsuarios(DatosRequest request, Authentication authentication) throws IOException {
		Usuario usuario = new Usuario();
		Gson gson = new Gson();

		String datosJson = String.valueOf(authentication.getPrincipal());
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);

		return providerRestTemplate.consumirServicio(usuario.obtenerUsuarios(request, busqueda).getDatos(),
				urlDominioGenerico + PAGINADO, authentication);

	}

	@SuppressWarnings("rawtypes")
	public Response<Object> buscarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);

		Response<Object> response = providerRestTemplate.consumirServicio(usuario.buscarUsuario(request).getDatos(),
				urlDominioGenerico + PAGINADO, authentication);
		ArrayList<?> datos1 = (ArrayList) ((LinkedHashMap) response.getDatos()).get("content");
		if (datos1.isEmpty()) {
			response.setMensaje(INFONOENCONTRADA);
		}

		try {
			return response;
		} catch (Exception e) {
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return null;
		}
	}

	@Override
	public Response<Object> consistCurp(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		usuario.setNombre(usuarioRequest.getNombre().toUpperCase());
		usuario.setPaterno(usuarioRequest.getPaterno().toUpperCase());
		usuario.setMaterno(usuarioRequest.getMaterno().toUpperCase());
		usuario.setCurp(usuarioRequest.getCurp().toUpperCase());

		return new Response<>(false, HttpStatus.OK.value(), "Exito",
				ConvertirGenerico.convertInstanceOfObject(usuario.consistenciaCurp()));

	}




	@Override
	public Response<Object> consultaSiap(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		Response<Object> response;

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		response = providerRestTemplate.consumirServicio(usuario.consultaParamSiap(request).getDatos(),
				urlDominioGenerico + CONSULTA, authentication);
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

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> consultaRenapo(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		Response<Object> response;

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		Usuario usuario = new Usuario(usuarioRequest);
		response = providerRestTemplate.consumirServicio(usuario.consultaParamRenapo(request).getDatos(),
				urlDominioGenerico + CONSULTA, authentication);
		ArrayList<LinkedHashMap> datosResp = (ArrayList<LinkedHashMap>) response.getDatos();
		if (datosResp.isEmpty()) {
			log.error("No se encuentra parámetro");
		} else if (datosResp.get(0).get(AppConstantes.VALOR).toString().equals("0")) {
			log.info("No buscar en RENAPO");
		} else {
			log.info("Buscar en RENAPO");
		}

		return response;
	}

	@Override
	public Response<Object> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto reporteDto = gson.fromJson(datosJson, BusquedaDto.class);

		Map<String, Object> envioDatos = new Usuario().generarReporte(reporteDto, NOMBRE_PDF_REPORTES);

		return providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
	}

	/*
	 * *********************** ***********************
	 *************************/
	

	@Override
	public Response<Object> consUsuarios(DatosRequest request, Authentication authentication) throws IOException {			
			Page<Map<String, Object>> objetoMapeado = null;
			Integer pagina =  Integer.parseInt( request.getDatos().get("pagina").toString() );
			Integer tamanio =  Integer.parseInt( request.getDatos().get("tamanio").toString() );

			String query = usuarioQuerys.queryConsultaUsuarios(request);
				objetoMapeado = PaginadoUtil.paginado(pagina, tamanio, query);
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, objetoMapeado);

	}

	@Override
	public Response<Object> buscarUsuarios(DatosRequest request, Authentication authentication) throws IOException {
		
		Page<Map<String, Object>> objetoMapeado = null;
		Integer pagina =  Integer.parseInt( request.getDatos().get("pagina").toString() );
		Integer tamanio =  Integer.parseInt( request.getDatos().get("tamanio").toString() );

		String query = usuarioQuerys.queryConsultaUsuarios(request);
		objetoMapeado = PaginadoUtil.paginado(pagina, tamanio, query);
			
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, objetoMapeado);
	}

	@Override
	public Response<Object> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException {

		String where = " WHERE su.ID_USUARIO = " + request.getDatos().get("id").toString();
		List<Map<String, Object>> resp = null;
		SqlSessionFactory sqlSessionFactory = MyBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UsuarioMapper usuarioMapper = session.getMapper(UsuarioMapper.class);
			try {
				resp = usuarioMapper.detalleUsuario(where);
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			}
		}
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resp);
	}

	@Override
	public Response<Object> validaCurp(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		List<Map<String, Object>> resp = null;
		SqlSessionFactory sqlSessionFactory = MyBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UsuarioMapper usuarioMapper = session.getMapper(UsuarioMapper.class);
			try {
				resp = usuarioMapper.validaCurp(usuarioRequest.getCurp());
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			}
		}
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resp);
	}

	@Override
	public Response<Object> validaMatricula(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		List<Map<String, Object>> resp = null;
		SqlSessionFactory sqlSessionFactory = MyBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UsuarioMapper usuarioMapper = session.getMapper(UsuarioMapper.class);
			try {
				resp = usuarioMapper.validaMatricula(usuarioRequest.getClaveMatricula());
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			}
		}
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resp);
	}

	@Override
	public Response<Object> catalogoRoles(DatosRequest request, Authentication authentication) throws IOException {
		List<Map<String, Object>> resp = null;
		SqlSessionFactory sqlSessionFactory = MyBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UsuarioMapper usuarioMapper = session.getMapper(UsuarioMapper.class);
			try {
				resp = usuarioMapper.catalogoRoles(request.getDatos().get("id").toString());
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			}
		}
		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resp);
	}

	@Override
	public Response<Object> agregarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		UsuarioEntity usu = new UsuarioEntity();
		PersonaEntity per = new PersonaEntity();

		Integer espacio = usuarioRequest.getNombre().indexOf(' ');
		String primerNombre = (espacio == -1) ? usuarioRequest.getNombre() : usuarioRequest.getNombre().substring(0, espacio);
		CharSequence contrasena = generaContrasena(primerNombre, usuarioRequest.getPaterno());
		
		per.setCveRFC(usuarioRequest.getRfc());
		per.setCveCURP(usuarioRequest.getCurp());
		per.setCveNSS(usuarioRequest.getNss());
		per.setNombre(usuarioRequest.getNombre());
		per.setPaterno(usuarioRequest.getPaterno());
		per.setMaterno(usuarioRequest.getMaterno());
		per.setNumSexo(usuarioRequest.getNumsexo());
		per.setDesOtroSexo(usuarioRequest.getDesOtroSexo());
		per.setFecNac(usuarioRequest.getFecNac());
		per.setIdPais(usuarioRequest.getIdPais());
		per.setIdEstado(usuarioRequest.getIdEstado());
		per.setDesTelefono(usuarioRequest.getDesTelefono());
		per.setDesTelefonoFijo(usuarioRequest.getDesTelefonoFijo());
		per.setCorreo(usuarioRequest.getCorreo());
		per.setTipPersona(usuarioRequest.getTipPersona());
		per.setNumINE(usuarioRequest.getNumINE());

		usu.setMatricula(usuarioRequest.getClaveMatricula());
		usu.setIdOficina(usuarioRequest.getIdOficina());
		usu.setIdDelegacion(usuarioRequest.getIdDelegacion());
		usu.setIdVelatorio(usuarioRequest.getIdVelatorio());
		
		usu.setIdRol(usuarioRequest.getIdRol());
		usu.setCveUsuario(usuarioRequest.getCveUsuario() == null ? "" : usuarioRequest.getCveUsuario());
		usu.setCveContrasenia(usuarioRequest.getCveContasenia() == null ? "" : usuarioRequest.getCveContasenia());
		usu.setIndContratante(1);
		usu.setIndActivo(1);
		usu.setIdUsuarioAlta(usuarioDto.getIdUsuario() == null ? 0 : usuarioDto.getIdUsuario());
		usu.setCveContrasenia(passwordEncoder.encode(contrasena));
		int resp = 0;
		SqlSessionFactory sqlSessionFactory = MyBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			PersonaMapper personaMapper = session.getMapper(PersonaMapper.class);
			UsuarioMapper usuarioMapper = session.getMapper(UsuarioMapper.class);
			try {
				usu.setCveUsuario(generarUsuario(primerNombre, usuarioRequest.getPaterno(), usuarioMapper.obtenerTotalUsuarios()));
				personaMapper.agregarPersona(per);
				usu.setIdPersona(per.getIdPersona());
				usuarioMapper.agregarNuevoUsuario(usu);
				resp = usu.getIdUsuario();
				session.commit();
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			}
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resp);
	}

	@Override
	public Response<Object> actualizarUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		UsuarioEntity usu = new UsuarioEntity();
		PersonaEntity per = new PersonaEntity();

		per.setCorreo(usuarioRequest.getCorreo());
		per.setIdUsuarioModifica(usuarioDto.getIdUsuario() == null ? 0 : usuarioDto.getIdUsuario());
		
		usu.setIdUsuario(usuarioRequest.getId());
		usu.setMatricula(usuarioRequest.getClaveMatricula());
		usu.setIdOficina(usuarioRequest.getIdOficina());
		usu.setIdDelegacion(usuarioRequest.getIdDelegacion());
		usu.setIdVelatorio(usuarioRequest.getIdVelatorio());
		
		usu.setIdRol(usuarioRequest.getIdRol());
		usu.setCveUsuario(usuarioRequest.getCveUsuario() == null ? "" : usuarioRequest.getCveUsuario());
		usu.setCveContrasenia(usuarioRequest.getCveContasenia() == null ? "" : usuarioRequest.getCveContasenia());
		usu.setIndContratante(1);
		usu.setIndActivo(usuarioRequest.getEstatus());
		usu.setIdUsuarioMidifica(usuarioDto.getIdUsuario() == null ? 0 : usuarioDto.getIdUsuario());
		int resp = 0;
		SqlSessionFactory sqlSessionFactory = MyBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			PersonaMapper personaMapper = session.getMapper(PersonaMapper.class);
			UsuarioMapper usuarioMapper = session.getMapper(UsuarioMapper.class);
			try {
				usuarioMapper.actualizarUsuario(usu);
				per.setIdPersona(usuarioMapper.obtenerIdPersona(usu.getIdUsuario()));
				personaMapper.actualizarPersona(per);
				resp = usu.getIdUsuario();
				session.commit();
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			}
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resp);
	}

	@Override
	public Response<Object> cambiarEstatusUsuario(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		int resp = 0;
		UsuarioEntity usu = new UsuarioEntity();
		usu.setIdUsuario(usuarioRequest.getId());
		usu.setIdUsuarioMidifica(usuarioDto.getIdUsuario() == null ? 0 : usuarioDto.getIdUsuario());
		SqlSessionFactory sqlSessionFactory = MyBatisConfig.buildqlSessionFactory();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UsuarioMapper usuarioMapper = session.getMapper(UsuarioMapper.class);
			try {
				resp =usuarioMapper.actualizarEstatusUsuario(usu);
				session.commit();
			} catch (Exception e) {
				e.printStackTrace();
				session.rollback();
			}
		}

		return new Response<>(false, HttpStatus.OK.value(), AppConstantes.EXITO, resp);
	}
	

	private String generaContrasena(String primerNombre, String paterno) {
		char[] caracterEsp = { '#', '$', '^', '+', '=', '!', '*', '(', ')', '@', '%', '&' };
		String mes = String.format("%02d", Calendar.getInstance().get(Calendar.MONTH) + 1);
		String formato = paterno.toUpperCase().charAt(0) + paterno.substring(1, paterno.length()).toLowerCase();

		return primerNombre.concat(String.valueOf(caracterEsp[new SecureRandom().nextInt(11)])).concat(".")
				.concat(formato.substring(0, 2)).concat(mes);
	}

	private String generarUsuario(String primerNombre, String paterno, Integer consecutivo) {
		return primerNombre.concat(paterno.substring(0, 1)).concat(String.format("%03d", consecutivo + 1));
	}

}
