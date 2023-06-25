package com.encrypt.filter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultServletInputStream extends ServletInputStream {

    private final InputStream inputStream;
    private boolean finished = false;

    DefaultServletInputStream(InputStream inputStream) {
        if (inputStream == null) {
            throw new RuntimeException("inputStream must not be null");
        }
        this.inputStream = inputStream;
    }


    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int available() throws IOException {
        return this.inputStream.available();
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.inputStream.close();
    }

    @Override
    public int read() throws IOException {
        int data = this.inputStream.read();
        if (data == -1) {
            this.finished = true;
        }
        return data;
    }
}
