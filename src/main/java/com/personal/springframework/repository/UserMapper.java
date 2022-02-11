package com.personal.springframework.repository;

import com.personal.springframework.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:UserMapper
 * @author: anshaojun
 * @time: 2021-05-18 16:42
 */
@Mapper
public interface UserMapper extends BaseMapper<User>{

    User loginCheck(User user);

    User userCheck(User user);
}
