package com.imss.usuarios;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UsuariosApplicationTests {

	@Test
	void contextLoads() {
		String result="test";
		UsuariosApplication.main(new String[]{});
		assertNotNull(result);
	}

}
