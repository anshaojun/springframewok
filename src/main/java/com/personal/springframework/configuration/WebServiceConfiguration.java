package com.personal.springframework.configuration;

import com.personal.springframework.webservice.TestSoap;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

/**
 * @program: springframework
 * @description: webservice配置
 * @author: 安少军
 * @create: 2021-12-27 10:56
 **/
@Configuration
public class WebServiceConfiguration {
    @Resource
    private Bus bus;

    @Bean
    public Endpoint endpoint(TestSoap testSoap) {
        Endpoint endpoint = new EndpointImpl(bus, testSoap);
        endpoint.publish("/test");
        return endpoint;
    }

    @Bean
    public ServletRegistrationBean dispatcherServletCfx() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new CXFServlet(), "/webservice/*");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }
}
