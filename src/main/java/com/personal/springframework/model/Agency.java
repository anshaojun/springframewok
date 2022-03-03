package com.personal.springframework.model;

import com.personal.springframework.model.core.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: springframework
 * @description: 单位
 * @author: 安少军
 * @create: 2021-12-30 10:43
 **/
@Data
public class Agency extends BaseEntity {
    private String agencyCode;
    private String agencyName;
    private String parentId;
    private String isLeaf;
    private String mLevel;
    private String type;
    private Agency parent;
    private List<Agency> child = new ArrayList<>();
}
