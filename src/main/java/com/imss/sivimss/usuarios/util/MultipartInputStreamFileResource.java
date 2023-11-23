package com.imss.sivimss.usuarios.util;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;

public class MultipartInputStreamFileResource extends InputStreamResource {

    private final String filename;
	private Object inputStream;

    MultipartInputStreamFileResource(InputStream inputStream, String filename) {
        super(inputStream);
        this.filename = filename;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public long contentLength() throws IOException {
        return -1; // we do not want to generally read the whole stream into memory ...
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
          return true;
        }
        else if (!(o instanceof MultipartInputStreamFileResource)) {
          return false;
        }
		return false;
      }

	@Override
	public int hashCode() {
		return this.inputStream.hashCode();
	}
}
