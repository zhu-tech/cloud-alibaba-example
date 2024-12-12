package com.neyogoo.example.biz.toolbox.vo.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class CustomMultipartFile implements MultipartFile {

    private String contentType;

    private String originalFilename;

    private String name;

    private byte[] bytes;

    public CustomMultipartFile(String name, String originalFilename, String contentType, byte[] bytes) {
        this.bytes = bytes;
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
    }

    public CustomMultipartFile(String originalFilename, byte[] bytes) {
        this.bytes = bytes;
        this.name = "file";
        this.originalFilename = originalFilename;
        this.contentType = "multipart/form-data";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return bytes == null || bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    @SuppressWarnings("resource")
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(bytes);
    }
}
