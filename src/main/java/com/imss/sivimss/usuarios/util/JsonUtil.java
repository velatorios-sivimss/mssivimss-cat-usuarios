package com.imss.sivimss.usuarios.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class JsonUtil {
	private static Logger LOG = LoggerFactory.getLogger(JsonUtil.class);
    private JsonUtil() {
    }

    public static String readFromJson(String filename) {
        try {
            File file = ResourceUtils.getFile("classpath:"+ filename);
            return new String(Files.readAllBytes(file.toPath()));
        } catch (FileNotFoundException e) {
            LOG.error("Exception in : readFromJson(): File Not Found: " + filename);
        } catch (IOException e) {
            LOG.error("Exception in : readFromJson(): IOException: " + e.getMessage());
        }
        return null;
    }

}
