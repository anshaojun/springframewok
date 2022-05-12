package com.personal.springframework.repository;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: springframework
 * @description: mapper
 * @author: 安少军
 * @create: 2021-12-29 11:57
 **/
public abstract interface AbstractMapper<T> {

    List<T> getByPage(T clazz);

    T getById(String id);

    List<T> getByParam(Map<String, Object> param);

    List<T> getByUniqueParam(@Param("column") String column, @Param("value") Object value);

    int insert(T clazz);

    int update(T clazz);

    int delete(String id);

    int deleteLogical(T clazz);

    int batchDelete(List<T> l);
}
