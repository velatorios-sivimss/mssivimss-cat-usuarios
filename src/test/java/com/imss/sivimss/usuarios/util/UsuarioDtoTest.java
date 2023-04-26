package com.imss.sivimss.usuarios.util;

import org.junit.Test;

import com.imss.sivimss.usuarios.model.request.UsuarioDto;

import static org.junit.jupiter.api.Assertions.assertNull;

public class UsuarioDtoTest {
    @Test
    public void usuarioDtoTest() throws Exception {
        UsuarioDto request=new UsuarioDto();
        assertNull(request.getIdUsuario());
        assertNull(request.getDesRol());
        assertNull(request.getNombre());
        assertNull(request.getNombre());
    }
}
