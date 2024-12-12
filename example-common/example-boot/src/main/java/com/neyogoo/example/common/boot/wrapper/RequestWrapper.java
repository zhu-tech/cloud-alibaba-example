package com.neyogoo.example.common.boot.wrapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Slf4j
public class RequestWrapper extends HttpServletRequestWrapper {

    /**
     * 存储 body 数据的容器
     */
    private final byte[] body;

    public RequestWrapper(HttpServletRequest request) {
        super(request);
        String bodyStr = getBodyString(request);
        body = bodyStr.getBytes(Charset.defaultCharset());
    }

    /**
     * 获取请求Body
     */
    @SneakyThrows
    public String getBodyString(final ServletRequest request) {
        return inputStream2String(request.getInputStream());
    }

    /**
     * 获取请求Body
     */
    public String getBodyString() {
        return inputStream2String(new ByteArrayInputStream(body));
    }

    /**
     * 将 inputStream 里的数据读取出来并转换成字符串
     */
    private String inputStream2String(InputStream inputStream) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {

        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }
}