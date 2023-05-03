package com.imss.sivimss.usuarios.controller;

import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.imss.sivimss.usuarios.base.BaseTest;
import com.imss.sivimss.usuarios.client.MockModCatalogosClient;
import com.imss.sivimss.usuarios.security.jwt.JwtTokenProvider;
import com.imss.sivimss.usuarios.util.JsonUtil;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@WithMockUser(username="10796223", password="123456",roles = "ADMIN")
public class UsuarioControllerTest extends BaseTest {
	 @Autowired
	 private JwtTokenProvider jwtTokenProvider;

	 @BeforeEach
	 public void setup() {
	    this.mockMvc = MockMvcBuilders
	                .webAppContextSetup(this.context)
	                .apply(springSecurity())
	                .build();
	 }
	 
	 @Test
	 @DisplayName("valida curp")
	 @Order(1)
	 public void validaCurpOK() throws Exception {
	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       String myToken = jwtTokenProvider.createTokenTest(authentication.getPrincipal().toString());
	       MockModCatalogosClient.validaCurp(HttpStatusCode.OK_200, JsonUtil.readFromJson("json/request/curp_usuario_mock.json"), JsonUtil.readFromJson("json/response/response_curp_usuario.json"), myToken, mockServer);
	       this.mockMvc.perform(post("/usuarios/valida-curp")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON)
	                    .header("Authorization","Bearer " + myToken)
	                    .content(JsonUtil.readFromJson("json/request/curp_usuario_controller.json"))
	                    .with(csrf()))
	                .andDo(print())
	                .andExpect(status().isOk());
	 }
	 
	 
	 @Test
	 @DisplayName("agregar usuario")
	 @Order(2)
	 public void crearUsuarioOK() throws Exception {
	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       String myToken = jwtTokenProvider.createTokenTest(authentication.getPrincipal().toString());
	       MockModCatalogosClient.agregarUsuario(HttpStatusCode.OK_200, JsonUtil.readFromJson("json/request/crear_usuario_mock.json"), JsonUtil.readFromJson("json/response/response_crear_usuario.json"), myToken, mockServer);
	       this.mockMvc.perform(post("/usuarios/agregar")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON)
	                    .header("Authorization","Bearer " + myToken)
	                    .content(JsonUtil.readFromJson("json/request/crear_usuario_controller.json"))
	                    .with(csrf()))
	                .andDo(print())
	                .andExpect(status().isOk());
	 }
	 
	 @Test
	 @DisplayName("actualizar usuario")
	 @Order(3)
	 public void actualizarUsuarioOK() throws Exception {
	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       String myToken = jwtTokenProvider.createTokenTest(authentication.getPrincipal().toString());
	       MockModCatalogosClient.actualizarUsuario(HttpStatusCode.OK_200, JsonUtil.readFromJson("json/request/actualizar_usuario_mock.json"), JsonUtil.readFromJson("json/response/response_actualizar_usuario.json"), myToken, mockServer);
	       this.mockMvc.perform(post("/usuarios/actualizar")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON)
	                    .header("Authorization","Bearer " + myToken)
	                    .content(JsonUtil.readFromJson("json/request/actualizar_usuario_controller.json"))
	                    .with(csrf()))
	                .andDo(print())
	                .andExpect(status().isOk());
	 }
	 
	 @Test
	 @DisplayName("estatus usuario")
	 @Order(4)
	 public void estatusUsuarioOK() throws Exception {
	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       String myToken = jwtTokenProvider.createTokenTest(authentication.getPrincipal().toString());
	       MockModCatalogosClient.estatusUsuario(HttpStatusCode.OK_200, JsonUtil.readFromJson("json/request/estatus_usuario_mock.json"), JsonUtil.readFromJson("json/response/response_estatus_usuario.json"), myToken, mockServer);
	       this.mockMvc.perform(post("/usuarios/cambiar-estatus")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON)
	                    .header("Authorization","Bearer " + myToken)
	                    .content(JsonUtil.readFromJson("json/request/estatus_usuario_controller.json"))
	                    .with(csrf()))
	                .andDo(print())
	                .andExpect(status().isOk());
	 }
	 
	 @Test
	 @DisplayName("buscar usuario")
	 @Order(5)
	 public void buscarUsuarioOK() throws Exception {
	       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	       String myToken = jwtTokenProvider.createTokenTest(authentication.getPrincipal().toString());
	       MockModCatalogosClient.buscarUsuario(HttpStatusCode.OK_200, JsonUtil.readFromJson("json/request/buscar_usuario_mock.json"), JsonUtil.readFromJson("json/response/response_buscar_usuario.json"), myToken, mockServer);
	       this.mockMvc.perform(post("/usuarios/buscar")
	                    .contentType(MediaType.APPLICATION_JSON)
	                    .accept(MediaType.APPLICATION_JSON)
	                    .header("Authorization","Bearer " + myToken)
	                    .content(JsonUtil.readFromJson("json/request/buscar_usuario_controller.json"))
	                    .with(csrf()))
	                .andDo(print())
	                .andExpect(status().isOk());
	 }

}
