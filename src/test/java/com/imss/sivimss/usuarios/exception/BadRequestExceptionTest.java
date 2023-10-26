package com.imss.sivimss.usuarios.exception;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.imss.sivimss.usuarios.exception.BadRequestException;

import static com.imss.sivimss.usuarios.util.UtUtils.createInstance;
import static com.imss.sivimss.usuarios.util.UtUtils.setField;
import static org.junit.Assert.assertNull;

public final class BadRequestExceptionTest {
    @Test
    public void testGetEstado_ReturnCodigo() throws Exception {
        BadRequestException badRequestException = ((BadRequestException) createInstance("com.imss.sivimss.usuarios.exception.BadRequestException"));
        setField(badRequestException, "com.imss.sivimss.usuarios.exception.BadRequestException", "codigo", null);

        HttpStatus actual = badRequestException.getCodigo();

        assertNull(actual);
    }

    @Test
    public void testGetMensaje_ReturnMensaje() throws Exception {
        BadRequestException badRequestException = ((BadRequestException) createInstance("com.imss.sivimss.usuarios.exception.BadRequestException"));
        setField(badRequestException, "com.imss.sivimss.usuarios.exception.BadRequestException", "mensaje", null);

        String actual = badRequestException.getMensaje();

        assertNull(actual);
    }
}
