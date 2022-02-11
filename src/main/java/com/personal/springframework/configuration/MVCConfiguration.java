package com.personal.springframework.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.personal.springframework.interceptor.core.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: anshaojun
 * @time: 2021-05-20 08:40
 */
@Configuration
public class MVCConfiguration implements WebMvcConfigurer, ErrorPageRegistrar {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(true)    //设置是否是后缀模式匹配,即:/test.*
                .setUseTrailingSlashMatch(true);    //设置是否自动后缀路径模式匹配,即：/test/
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/page/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean<DispatcherServlet> servletServletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet);
        servletServletRegistrationBean.addUrlMappings("*.do");
        servletServletRegistrationBean.addUrlMappings("*.action");
        servletServletRegistrationBean.addUrlMappings("*.html");
        servletServletRegistrationBean.addUrlMappings("*.css");
        servletServletRegistrationBean.addUrlMappings("*.js");
        servletServletRegistrationBean.addUrlMappings("*.png");
        servletServletRegistrationBean.addUrlMappings("*.gif");
        servletServletRegistrationBean.addUrlMappings("*.ico");
        servletServletRegistrationBean.addUrlMappings("*.jpeg");
        servletServletRegistrationBean.addUrlMappings("*.jpg");
        servletServletRegistrationBean.addUrlMappings("*.svg");
        servletServletRegistrationBean.addUrlMappings("*.eot");
        servletServletRegistrationBean.addUrlMappings("*.ttf");
        servletServletRegistrationBean.addUrlMappings("*.woff");
        servletServletRegistrationBean.addUrlMappings("*.woff2");
        return servletServletRegistrationBean;
    }

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();    //添加fastJson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);    //处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);    //在convert中添加配置信息.
        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter = fastJsonHttpMessageConverter;
        return new HttpMessageConverters(converter);
    }


    /**
     * @return void
     * @Author 安少军
     * @Description 注册静态资源位置，springboot下的静态资源可以放在：
     * classpath:/META-INF/resources/
     * classpath:/resources/
     * classpath:/static/
     * classpath:/public/
     * /
     * @Date 14:44 2021/12/30
     * @Param [registry]
     **/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:static/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:static/css/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:static/fonts/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:static/images/");
        registry.addResourceHandler("/html/**").addResourceLocations("classpath:static/html/");
    }

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage[] errorPages = new ErrorPage[1];
        errorPages[0] = new ErrorPage(HttpStatus.NOT_FOUND, "/html/404.html");
        registry.addErrorPages(errorPages);
    }

    @Bean
    public AccessLimitInterceptor accessLimitInterceptor(){
        return new AccessLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor());
    }
}
