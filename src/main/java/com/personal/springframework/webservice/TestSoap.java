package com.personal.springframework.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @program: springframework
 * @description: webservice
 * @author: 安少军
 * @create: 2021-12-27 11:02
 **/
@WebService(targetNamespace = "http://springframework.ws.com")
public interface TestSoap {
    @WebMethod(action = "http://springframework.ws.com/test")
    String test(@WebParam(name = "hello",targetNamespace = "http://springframework.ws.com") String hello);
}
