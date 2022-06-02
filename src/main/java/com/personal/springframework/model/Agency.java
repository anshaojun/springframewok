package com.personal.springframework.model;

import com.personal.springframework.model.core.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    //单位编码
    @NotNull(message = "单位编码不能为空")
    private String agencyCode;
    //单位名称
    @NotNull(message = "单位名称不能为空")
    @Size(max = 40,message = "单位名称最长为40")
    private String agencyName;
    //父节点ID
    private String parentId;
    //是否低级
    private String isLeaf;
    //等级
    private String mLevel;
    //类型
    @NotNull(message = "类型不能为空")
    @Size(max = 1,message = "类型最长为1")
    private String type;
    //父单位
    private Agency parent;
    //选中状态
    private Boolean checked;
    //子节点
    private List<Agency> child = new ArrayList<>();
}
