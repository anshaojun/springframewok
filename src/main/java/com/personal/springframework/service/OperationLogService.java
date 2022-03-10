package com.personal.springframework.service;

import com.personal.springframework.model.core.OperationLog;
import com.personal.springframework.repository.OperationLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:登录service
 * @author: anshaojun
 * @time: 2021-05-24 09:48
 */
@Service
@Slf4j
public class OperationLogService extends BaseService<OperationLog, OperationLogMapper> {

}
