--管理员密码admin@123~
INSERT INTO TBL_CORE_USER (id, USER_NAME, PASS_WORD, NICK_NAME, TEL, MAIL, LAST_LOGIN_TIME, LAST_LOGIN_IP, PORTRAIT)
VALUES ('0', 'admin', '4178d3f29660deb916adb14b77b250b8', '超级管理员', NULL, NULL, SYSDATE, '127.0.0.1',null);

INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('7494AA9478D84DF19F92938D6A28E411', '修改', '0AC926B0A3CC4A5889F77C87BFF79A67', 'sys:user:edit', '1', NULL, NULL,
        3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('0AC926B0A3CC4A5889F77C87BFF79A67', '用户管理', 'ee3ca7543c3d497fbc81837190ae56b7', 'sys:user:list', '0',
        'userManage/main.do', 'layui-icon-username', 2, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('073D015C55324B438BFC35EC82FA6E16', '新增', '0AC926B0A3CC4A5889F77C87BFF79A67', 'sys:user:add', '1', NULL, NULL,
        3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('4176364B945A4E92A03E6E9C8B629638', '删除', '0AC926B0A3CC4A5889F77C87BFF79A67', 'sys:user:del', '1', NULL, NULL,
        3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('29482EE5A0F54784A83F501787C9AB78', '单位权限', 'ee3ca7543c3d497fbc81837190ae56b9', 'sys:permission:addagency', '1',
        NULL, NULL, 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('ee3ca7543c3d497fbc81837190ae56b7', '系统管理', '0', 'sys:system:view', '0', NULL, 'layui-icon-set-fill', 1, '0');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('ee3ca7543c3d497fbc81837190ae56b8', '菜单管理', 'ee3ca7543c3d497fbc81837190ae56b7', 'sys:menu:list', '0',
        'menuManage/main.do', 'layui-icon-more', 2, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('ee3ca7543c3d497fbc81837190ae56b9', '权限管理', 'ee3ca7543c3d497fbc81837190ae56b7', 'sys:permission:list', '0',
        'roleManage/main.do', 'layui-icon-vercode', 2, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('E14E1938C09E475D9DE4E0C13C2CE8FA', '添加用户', 'ee3ca7543c3d497fbc81837190ae56b9', 'sys:permission:adduser', '1',
        NULL, 'layui-icon-chat', 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('A60ECBE9E35347EA8CC7045798CE1DF5', '菜单权限', 'ee3ca7543c3d497fbc81837190ae56b9', 'sys:permission:addmenu', '1',
        NULL, 'layui-icon-vercode', 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('6327803A30004C888C14DDD302B4C1B0', '修改', 'ee3ca7543c3d497fbc81837190ae56b9', 'sys:permission:edit', '1', NULL,
        'layui-icon-vercode', 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('4DFCC3984A0D4C6492417C0EA75A79E1', '删除', 'ee3ca7543c3d497fbc81837190ae56b9', 'sys:permission:del', '1', NULL,
        'layui-icon-vercode', 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('6E67AA4F24234B54AB3B4E70EF5C5802', '新增', 'ee3ca7543c3d497fbc81837190ae56b9', 'sys:permission:add', '1', NULL,
        'layui-icon-vercode', 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('ef3ca7543c3d497fbc81837190a256b2', '添加下级菜单', 'ee3ca7543c3d497fbc81837190ae56b8', 'sys:menu:add', '1', NULL,
        NULL, 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('ef3ca7543c3d497fbc81837190a256b1', '修改', 'ee3ca7543c3d497fbc81837190ae56b8', 'sys:menu:edit', '1', NULL, NULL,
        3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('ef3ca7543c3d497fbc81837190a256b3', '删除', 'ee3ca7543c3d497fbc81837190ae56b8', 'sys:menu:del', '1', NULL, NULL,
        3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('7BF1BCE6E85B44C3987DE1DAD469BDB0', '添加下级单位', '0372B9268F7A45F99AF64E9AA79D6D98', 'sys:agency:add', '1', NULL,
        NULL, 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('1C2CB366107440D2BEA59C0D8F978278', '修改', '0372B9268F7A45F99AF64E9AA79D6D98', 'sys:agency:edit', '1', NULL,
        NULL, 3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('D1E89D532FEB44AEA4C5EB6C7B9EA274', '删除', '0372B9268F7A45F99AF64E9AA79D6D98', 'sys:agency:del', '1', NULL, NULL,
        3, '1');
INSERT INTO TBL_CORE_MENU (id, menu_name, parent_id, permission, type, url, icon, mlevel, is_leaf)
VALUES ('0372B9268F7A45F99AF64E9AA79D6D98', '单位维护', 'ee3ca7543c3d497fbc81837190ae56b7', 'sys:agency:list', '0',
        'agencyManage/main.do', 'layui-icon-home', 2, '1');
insert into TBL_CORE_MENU (ID, MENU_NAME, PARENT_ID, PERMISSION, TYPE, URL, ICON, MLEVEL, IS_LEAF)
values ('336F3A63E4A643DAAEA55B16042C3BD0', '日志管理', '0', 'log:oplog:list', '0', 'logManager/main.do', 'layui-icon-survey', 1, '1');

INSERT INTO TBL_CORE_ROLE (id, role_name, permission)
VALUES ('admin', '超级管理员', '1');
INSERT INTO TBL_CORE_ROLE (id, role_name, permission)
VALUES ('default', '默认角色', '3');

INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('default', 'ee3ca7543c3d497fbc81837190ae56b7');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('default', 'ee3ca7543c3d497fbc81837190ae56b8');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('default', 'ef3ca7543c3d497fbc81837190a256b1');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '7494AA9478D84DF19F92938D6A28E411');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '073D015C55324B438BFC35EC82FA6E16');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '4176364B945A4E92A03E6E9C8B629638');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', 'ef3ca7543c3d497fbc81837190a256b2');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', 'ef3ca7543c3d497fbc81837190a256b1');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', 'ef3ca7543c3d497fbc81837190a256b3');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', 'E14E1938C09E475D9DE4E0C13C2CE8FA');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', 'A60ECBE9E35347EA8CC7045798CE1DF5');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '6327803A30004C888C14DDD302B4C1B0');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '4DFCC3984A0D4C6492417C0EA75A79E1');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '6E67AA4F24234B54AB3B4E70EF5C5802');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '7BF1BCE6E85B44C3987DE1DAD469BDB0');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '1C2CB366107440D2BEA59C0D8F978278');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', 'D1E89D532FEB44AEA4C5EB6C7B9EA274');
INSERT INTO TBL_CORE_ROLE_MENU (role_id, menu_id)
VALUES ('admin', '336F3A63E4A643DAAEA55B16042C3BD0');

INSERT INTO TBL_CORE_ROLE_USER (user_id, role_id)
VALUES ('86E6B573FB9E4125894AB0A066A46404', 'default');
INSERT INTO TBL_CORE_ROLE_USER (user_id, role_id)
VALUES ('0', 'admin');
COMMIT;





