package com.imss.sivimss.usuarios.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestTemplateUtil {

	private final RestTemplate restTemplate;

	public RestTemplateUtil(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * Env&iacute;a una petici&oacute;n con Body.
	 *
	 * @param url
	 * @param clazz
	 * @return
	 */

	public Response<Object> sendPostRequestByteArray(String url, EnviarDatosRequest body, Class<Object> clazz)
			throws IOException {
		return enviarPeticion(null,null, url, clazz, body);
	}

	/**
	 * Env&iacute;a una petici&oacute;n con Body y token.
	 *
	 * @param url
	 * @param clazz
	 * @return
	 */

	@SuppressWarnings("unchecked")
	public Response<Object> sendPostRequestByteArrayToken(String url, EnviarDatosRequest body, String subject,
			Class<?> clazz) {
		HttpHeaders headers = RestTemplateUtil.createHttpHeadersToken(subject);

		HttpEntity<Object> request = new HttpEntity<>(body, headers);
		ResponseEntity<Object> responseEntity = null;

		responseEntity = (ResponseEntity<Object>) restTemplate.postForEntity(url, request, clazz);

		return (Response<Object>) responseEntity.getBody();
	}

	/**
	 * Crea los headers para la petici&oacute;n falta agregar el tema de seguridad
	 * para las peticiones
	 *
	 * @return
	 */
	private static HttpHeaders createHttpHeaders() {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return header;
	}

	/**
	 * Crea los headers para la petici&oacute;n con token - falta agregar el
	 * tema de seguridad para las peticiones
	 *
	 * @return
	 */
	private static HttpHeaders createHttpHeadersToken(String subject) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set("Authorization", "Bearer " + subject);

		header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return header;
	}

	///////////////////////////////////////////////////// peticion con archivos
	/**
	 * Crea los headers para la petici&oacute;n con token  - falta agregar el
	 * tema de seguridad para las peticiones
	 *
	 * @return
	 */
	/**
	 * Env&iacute;a una petici&oacute;n con Body, archivos y token.
	 *
	 * @param url
	 * @param clazz
	 * @return
	 */

	public Response<Object> sendPostRequestByteArrayArchviosToken(String url, EnviarDatosArchivosRequest body,
			String subject, Class<?> clazz) throws IOException {
		return  enviarPeticion(body,subject, url, clazz, null);
	}

	private static HttpHeaders createHttpHeadersArchivosToken(String subject) {

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);
		header.setAccept(Arrays.asList(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON));
		header.set("Authorization", "Bearer " + subject);
		return header;
	}

//////////////////////////////////////////
	/**
	 * Enviar una peticion con Body para reportes.
	 *
	 * @param url
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Response<Object> sendPostRequestByteArrayReportesToken(String url, DatosReporteDTO body, String subject,
			Class<?> clazz) {
		HttpHeaders headers = RestTemplateUtil.createHttpHeadersToken(subject);

		HttpEntity<Object> request = new HttpEntity<>(body, headers);
		ResponseEntity<Object> responseEntity = null;
		responseEntity = (ResponseEntity<Object>) restTemplate.postForEntity(url, request, clazz);
		return (Response<Object>) responseEntity.getBody();

	}
	
	@SuppressWarnings("unchecked")
	private Response<Object> enviarPeticion (EnviarDatosArchivosRequest bodyArchivo, String subject, String url, Class<?> clazz, EnviarDatosRequest bodyRequest ) throws IOException {

		Response<Object> responseBody = new Response<>();
		try {
			ResponseEntity<Object> responseEntity = null;
			HttpHeaders headers = null;
			if (bodyArchivo != null) {
				headers = RestTemplateUtil.createHttpHeadersArchivosToken(subject);

				LinkedMultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

				for (MultipartFile file : bodyArchivo.getArchivos()) {
					if (!file.isEmpty()) {
						parts.add("files",
								new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
					}
				}

				parts.add("datos", bodyArchivo.getDatos());
				HttpEntity<LinkedMultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);
				responseEntity = (ResponseEntity<Object>) restTemplate.postForEntity(url, request, clazz);

			}
			else {
				headers = RestTemplateUtil.createHttpHeaders();
				HttpEntity<Object> request = new HttpEntity<>(bodyRequest, headers);
				responseEntity = (ResponseEntity<Object>) restTemplate.postForEntity(url, request, clazz);
			}
			if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
				// noinspection unchecked
				responseBody = (Response<Object>) responseEntity.getBody();
			} else {
				throw new IOException("Ha ocurrido un error al enviar");
			}
		} catch (IOException ioException) {
			throw ioException;
		} catch (Exception e) {
			log.error("Fallo al consumir el servicio, {}", e.getMessage());
			responseBody.setCodigo(HttpStatus.INTERNAL_SERVER_ERROR.value());
			responseBody.setError(true);
			responseBody.setMensaje(e.getMessage());
		}
		return responseBody;
	}
}
