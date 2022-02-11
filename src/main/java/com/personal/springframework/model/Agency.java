package com.personal.springframework.model;

import com.personal.springframework.model.core.BaseEntity;
import lombok.Data;

/**
 * @program: springframework
 * @description: 单位
 * @author: 安少军
 * @create: 2021-12-30 10:43
 **/
@Data
public class Agency extends BaseEntity {
    private String agencyId;
    private String agencyCode;
    private String agencyName;
    private String parentId;
    private Integer isLeaf;
    private String level;
    private String type;
}
