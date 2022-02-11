package com.personal.springframework.model;

import com.personal.springframework.model.core.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class Menu extends BaseEntity {
    //菜单名称
    @NotNull(message = "菜单名不能为空")
    @Size(min=1,max = 10,message = "菜单长度为1-10")
    private String menuName;
    //父菜单id
    private Menu parent;
    //权限标识
    @NotNull(message = "权限标识不能为空")
    @Size(max = 60,message = "权限标识最长为60")
    private String permission;
    //菜单类型
    @NotNull(message = "菜单类型不能为空")
    private String type;
    //url
    private String url;
    //图标
    private String icon;
    //等级
    private String mLevel;
    //子菜单
    private List<Menu> child = new ArrayList<>();
    //是否底级
    private String isLeaf;
}
