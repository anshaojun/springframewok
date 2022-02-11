package com.personal.springframework.service;

import cn.hutool.core.collection.CollectionUtil;
import com.personal.springframework.constant.MenuOptions;
import com.personal.springframework.exception.ServiceException;
import com.personal.springframework.model.Menu;
import com.personal.springframework.repository.MenuMapper;
import com.personal.springframework.repository.RoleMapper;
import com.personal.springframework.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: springframework
 * @description: 菜单管理
 * @author: 安少军
 * @create: 2022-01-10 14:53
 **/
@Service
@Transactional(readOnly = true)
public class MenuManageService extends BaseService<Menu, MenuMapper> {
    @Resource
    RoleMapper roleMapper;

    public Object loadMenuTree() {
        List<Menu> menus = mapper.loadMenuTree();
        List<Map<String, Object>> result = new ArrayList<>();
        return run(menus, result);
    }

    private Object run(List<Menu> menus, List<Map<String, Object>> result) {
        for (Menu menu : menus) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", menu.getId());
            map.put("title", menu.getMenuName());
            map.put("spread", true);
            List<Menu> child = menu.getChild();
            List<Map<String, Object>> children = new ArrayList<>();
            map.put("children", run(child, children));
            result.add(map);
        }
        return result;
    }

    @Transactional(readOnly = false)
    public void save(Menu menu) {
        try {
            //校验
            //父级为二级菜单，只能为按钮
            //存在父级，则更新父级的是否底级为否
            if (menu.getParent() != null && StringUtils.isNotBlank(menu.getParent().getId())) {
                if (menu.getMLevel() == MenuOptions.TWO_LEVEL.getId()) {
                    if (!menu.getMLevel().equals(MenuOptions.THREE_LEVEL.getId()) || !menu.getType().equals(MenuOptions.MENU.getId())) {
                        throw new ServiceException("二级菜单下只能添加按钮");
                    }
                }
                Menu parent = getById(menu.getParent().getId());
                if(parent.getType().equals(MenuOptions.BUTTON.getId())){
                    throw new ServiceException("按钮下无法添加元素");
                }
                if(menu.getType().equals(MenuOptions.MENU.getId())){
                    if (parent.getType().equals(MenuOptions.MENU.getId()) && parent.getIsLeaf().equals(MenuOptions.LEAF.getId())) {
                        parent.setIsLeaf(MenuOptions.NORLEAF.getId());
                        super.update(parent);
                    }
                }
            } else {
                Menu parent = new Menu();
                parent.setId("0");
                menu.setParent(parent);
                if (!menu.getMLevel().equals(MenuOptions.ONE_LEVEL.getId())) {
                    throw new ServiceException("当前菜单无父级，菜单等级错误");
                }
                if (menu.getType().equals(MenuOptions.BUTTON.getId())) {
                    throw new ServiceException("不能在根目录下添加按钮");
                }
            }
            if (CollectionUtil.isEmpty(menu.getChild())) {
                if (menu.getIsLeaf().equals(MenuOptions.NORLEAF)) {
                    throw new ServiceException("菜单无子菜单，必须为底级");
                }
            }
            if (menu.isNew()) {
                super.insert(menu);
            } else {
                super.update(menu);
            }
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(String id) {
        try {
            //删除，级联删除菜单角色关联、子菜单
            super.delete(id);
            mapper.deleteByParent(id);
            roleMapper.deleteMenuRole(id);
        } catch (ServiceException se) {
            se.printStackTrace();
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("内部数据错误");
        }
    }
}
