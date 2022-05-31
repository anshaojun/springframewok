package com.personal.springframework.model.core;


import com.personal.springframework.util.StringUtils;
import lombok.Data;

import java.util.Date;

/**
 * @program: springframework
 * @description:
 * @author: 安少军
 * @create: 2021-12-29 10:49
 **/
@Data
public class BaseEntity extends Page {

    private String id;

    private String agencyId;

    private String createUser;

    private String createAgency;

    private Date createTime;

    private String updateUser;

    private String updateAgency;

    private Date updateTime;

    private String authSql;

    public boolean isNew() {
        if (StringUtils.isNotBlank(getId())) {
            return false;
        } else {
            return true;
        }
    }

}
