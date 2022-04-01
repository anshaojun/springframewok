package com.personal.springframework.repository;

import com.personal.springframework.model.core.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: springframewok
 * @description: 操作日志DAO
 * @author: 安少军
 * @create: 2022-03-10 14:54
 **/
@Mapper
public interface OperationLogMapper extends AbstractMapper<OperationLog> {
}
