package com.encrypt.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class DefaultServletOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream bos = null;

    public DefaultServletOutputStream(ByteArrayOutputStream bos) {
        this.bos = bos;
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(int b) throws IOException {
        bos.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        bos.write(b, 0, b.length);
    }
}
