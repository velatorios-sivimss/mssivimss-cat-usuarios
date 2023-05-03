package com.imss.sivimss.usuarios.util;

import org.junit.Test;

import com.imss.sivimss.usuarios.util.EnviarDatosRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EnviarDatosRequestTest {
    @Test
    public void enviarDatosRequestTest() throws Exception {
        Map<String, Object> datos=new HashMap<>();
        datos.put("nombre","vacio");
        EnviarDatosRequest request=new EnviarDatosRequest(datos);
        assertNotNull(request);
    }
}