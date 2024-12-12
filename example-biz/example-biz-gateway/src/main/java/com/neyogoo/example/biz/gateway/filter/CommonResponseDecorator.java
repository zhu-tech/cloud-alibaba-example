package com.neyogoo.example.biz.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

@Slf4j
public class CommonResponseDecorator extends ServerHttpResponseDecorator {

    private DataBufferFactory bufferFactory;

    public CommonResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
        this.bufferFactory = delegate.bufferFactory();
    }

    @Override
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            this.getDelegate().setStatusCode(HttpStatus.OK);
            MediaType contentType = getDelegate().getHeaders().getContentType();

            return super.writeWith(fluxBody.map(dataBuffer -> {
                // probably should reuse buffers
                byte[] content = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(content);
                // 释放掉内存
                DataBufferUtils.release(dataBuffer);
                if (MediaType.APPLICATION_JSON.equals(contentType)) {
                    String rs = new String(content, Charset.forName("UTF-8"));
                    log.info(rs);
                }
                return bufferFactory.wrap(content);
            }));
        }
        return super.writeWith(body);
    }
}
