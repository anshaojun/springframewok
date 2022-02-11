package com.personal.springframework.service;


import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.core.BaseEntity;
import com.personal.springframework.model.core.Page;
import com.personal.springframework.repository.BaseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @program: springframework
 * @description: service
 * @author: 安少军
 * @create: 2021-12-29 11:50
 **/
public abstract class BaseService<T extends BaseEntity, M extends BaseMapper<T>> {

    //使用@Autowired自动装配
    @Autowired(required = false)
    protected M mapper;

    @Transactional(readOnly = true)
    public Page<T> findPage(T clazz) {
        List<T> result = mapper.getByPage(clazz);
        Page page = new Page();
        BeanUtils.copyProperties(clazz, page);
        page.setList(result);
        return page;
    }

    @Transactional(readOnly = true)
    public T getById(String id) {
        return mapper.getById(id);
    }


    @Transactional(readOnly = false)
    public void delete(String id) {
        mapper.delete(id);
    }

    @Transactional(readOnly = false)
    public void insert(T clazz) {
        mapper.insert(clazz);
    }

    @Transactional(readOnly = false)
    public void update(T clazz) {
        mapper.update(clazz);
    }

    @Transactional(readOnly = false)
    public void saveNotExists(T clazz, String column, String property, String text) {
        Field[] fields = clazz.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            if (name.equals(property)) {
                try {
                    Object value = field.get(clazz);
                    List<T> exists = mapper.getByUniqueParam(column, value);
                    if (CollectionUtil.isNotEmpty(exists)) {
                        if (clazz.isNew()) {
                            throw new ServiceException(text + "重复");
                        } else {
                            boolean exist = exists.stream().filter(o -> o.getId().equals(clazz.getId())).findAny().isPresent();
                            if (!exist) {
                                throw new ServiceException(text + "重复");
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        if (clazz.isNew()) {
            mapper.insert(clazz);
        } else {
            mapper.update(clazz);
        }
    }

    @Transactional(readOnly = false)
    public void batchDelete(List<T> deletes){
        List<List<T>> groups = Lists.partition(deletes,50);
        groups.forEach(l->{
            mapper.batchDelete(l);
        });
    }

}
