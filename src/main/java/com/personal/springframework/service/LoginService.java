package com.personal.springframework.service;

import com.personal.springframework.annotation.OperLog;
import com.personal.springframework.model.User;
import com.personal.springframework.model.enums.OperModel;
import com.personal.springframework.model.enums.OperType;
import com.personal.springframework.repository.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description:登录service
 * @author: anshaojun
 * @time: 2021-05-24 09:48
 */
@Service
@Slf4j
public class LoginService extends BaseService<User, UserMapper> {
    /**
     * 继承抽象类：相当于拷贝一份父类的成员变量及方法到子类，在父类中使用@Autowired"注入（装配）"的TestMapper对象其实是装配到子类中,
     * 如果成员变量是私有，也会继承到子类，只是不能调用
     */
    public User loginCheck(User user) {
        return mapper.loginCheck(user);
    }

    public User userCheck(User user) {
        return mapper.userCheck(user);
    }

    @OperLog(operType = OperType.QUERY,operModel = OperModel.LOGIN,operDesc = "登录")
    public void updateLoginInfo(User user) {
        try {
            mapper.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
