package com.personal.springframework.configuration;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.personal.springframework.interceptor.core.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:mvc配置
 * @author: anshaojun
 * @time: 2021-05-20 08:40
 */
@Configuration
@ConfigurationProperties(prefix = "resolver")
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

    /**
     * @Author 安少军
     * @Description 设置视图层前后缀
     * @Date 15:51 2022/3/1
     * @Param []
     * @return org.springframework.web.servlet.view.InternalResourceViewResolver
     **/
    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/page/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    /**
     * @Author 安少军
     * @Description 设置请求后缀
     * @Date 15:52 2022/3/1
     * @Param [dispatcherServlet]
     * @return org.springframework.boot.web.servlet.ServletRegistrationBean
     **/
    @Bean
    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean<DispatcherServlet> servletServletRegistrationBean = new ServletRegistrationBean<>(dispatcherServlet);
        servletServletRegistrationBean.addUrlMappings("*.do");
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

    /**
     * @Author 安少军
     * @Description 注册404页面
     * @Date 15:52 2022/3/1
     * @Param [registry]
     * @return void
     **/
    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage[] errorPages = new ErrorPage[1];
        errorPages[0] = new ErrorPage(HttpStatus.NOT_FOUND, "/html/404.html");
        registry.addErrorPages(errorPages);
    }

    /**
     * @Author 安少军
     * @Description 过滤器
     * @Date 15:53 2022/3/1
     * @Param []
     * @return com.personal.springframework.interceptor.core.AccessLimitInterceptor
     **/
    @Bean
    public AccessLimitInterceptor accessLimitInterceptor(){
        return new AccessLimitInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLimitInterceptor());
    }


    @Value("${resolver.defaultEncoding}")
    private String defaultEncoding;
    @Value("${resolver.resolveLazily}")
    private boolean resolveLazily;
    @Value("${resolver.maxInMemorySize}")
    private int maxInMemorySize;
    @Value("${resolver.maxUploadSize}")
    private long maxUploadSize;

    /**
     * @Author 安少军
     * @Description 文件上传
     * @Date 15:53 2022/3/1
     * @Param []
     * @return org.springframework.web.multipart.MultipartResolver
     **/
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setDefaultEncoding(defaultEncoding);
        //resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        resolver.setResolveLazily(resolveLazily);
        resolver.setMaxInMemorySize(maxInMemorySize);
        //上传文件大小 50M 50*1024*1024
        resolver.setMaxUploadSize(maxUploadSize);
        return resolver;
    }

}
