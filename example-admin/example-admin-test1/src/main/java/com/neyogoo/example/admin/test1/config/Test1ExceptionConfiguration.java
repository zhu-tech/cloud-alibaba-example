package com.neyogoo.example.admin.test1.config;

import com.neyogoo.example.common.boot.handler.AbstractGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;

/**
 * 全局异常处理
 */
@Configuration
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class Test1ExceptionConfiguration extends AbstractGlobalExceptionHandler {

}
