package com.personal.springframework.webservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * @program: springframework
 * @description: webservice
 * @author: 安少军
 * @create: 2021-12-27 11:07
 **/
@WebService(
        targetNamespace = "http://springframework.ws.com",
        serviceName = "TestService",
        endpointInterface = "com.personal.springframework.webservice.TestSoap"
)
@Component
@Slf4j
public class TestSoapImpl implements TestSoap {
    @Override
    public String test(String hello) {
        log.info(hello);
        return "success";
    }
}
