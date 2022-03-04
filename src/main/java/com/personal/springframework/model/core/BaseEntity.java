package com.personal.springframework.model.core;


import com.personal.springframework.util.StringUtils;
import lombok.Data;

/**
 * @program: springframework
 * @description:
 * @author: 安少军
 * @create: 2021-12-29 10:49
 **/
@Data
public class BaseEntity extends Page {
    private String id;

    public boolean isNew() {
        if (StringUtils.isNotBlank(getId())) {
            return false;
        } else {
            return true;
        }
    }

    private String dataScope;
}
