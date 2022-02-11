-- Create table
create table TBL_CORE_USER
(
    id         VARCHAR2(32) not null,
    user_name       VARCHAR2(100) not null,
    pass_word       VARCHAR2(200) not null,
    nick_name       VARCHAR2(100) not null,
    tel             VARCHAR2(20),
    mail            VARCHAR2(30),
    last_login_time DATE,
    last_login_ip   VARCHAR2(30),
    portrait        clob
);
-- Add comments to the columns
comment on column TBL_CORE_USER.id
  is '主键';
comment on column TBL_CORE_USER.user_name
  is '用户名';
comment on column TBL_CORE_USER.pass_word
  is '密码';
comment on column TBL_CORE_USER.nick_name
  is '昵称';
comment on column TBL_CORE_USER.tel
  is '电话';
comment on column TBL_CORE_USER.mail
  is '邮箱';
comment on column TBL_CORE_USER.last_login_time
  is '上次登陆时间';
comment on column TBL_CORE_USER.last_login_ip
  is '上次登陆ip';
comment on column TBL_CORE_USER.portrait
    is '头像';
-- Create/Recreate indexes
create unique index PK_TBL_CORE_USER_1 on TBL_CORE_USER (USER_NAME);
create index PK_TBL_CORE_USER_2 on TBL_CORE_USER (USER_NAME, PASS_WORD);
-- Create/Recreate primary, unique and foreign key constraints
alter table TBL_CORE_USER
    add constraint PK_TBL_CORE_USER primary key (id);

-- Create table
create table TBL_CORE_MENU
(
    id    VARCHAR2(32) not null,
    menu_name  VARCHAR2(30) not null,
    parent_id  VARCHAR2(32) not null,
    permission VARCHAR2(60),
    type       VARCHAR2(1) not null,
    url        VARCHAR2(100),
    icon       VARCHAR2(30),
    mlevel     NUMBER(1) not null,
    is_leaf    VARCHAR2(1) not null
);
-- Add comments to the columns
comment on column TBL_CORE_MENU.id
    is 'id';
comment on column TBL_CORE_MENU.menu_name
    is '名称';
comment on column TBL_CORE_MENU.parent_id
    is '父级id';
comment on column TBL_CORE_MENU.permission
    is '权限标识';
comment on column TBL_CORE_MENU.type
    is '类型，0菜单1按钮';
comment on column TBL_CORE_MENU.url
    is '访问地址';
comment on column TBL_CORE_MENU.icon
    is '图标fa字体';
comment on column TBL_CORE_MENU.mlevel
    is '等级';
comment on column TBL_CORE_MENU.is_leaf
    is '是否底级';
-- Create/Recreate primary, unique and foreign key constraints
alter table TBL_CORE_MENU
    add constraint PK_TBL_CORE_MENU primary key (id);

-- Create table
create table TBL_CORE_ROLE
(
    id   VARCHAR2(32) not null,
    role_name VARCHAR2(60) not null
);
-- Add comments to the columns
comment on column TBL_CORE_ROLE.id
    is 'id';
comment on column TBL_CORE_ROLE.role_name
    is '角色名称';
-- Create/Recreate primary, unique and foreign key constraints
alter table TBL_CORE_ROLE
    add constraint PK_TBL_CORE_ROLE primary key (id);
create unique index IDX_TBL_CORE_ROLE_1 on TBL_CORE_ROLE (ROLE_NAME);
create table TBL_CORE_USER_ROLE
(
    user_id VARCHAR2(32),
    role_id VARCHAR2(32)
);
-- Add comments to the columns
comment on column TBL_CORE_USER_ROLE.user_id
    is '用户id';
comment on column TBL_CORE_USER_ROLE.role_id
    is '角色id';
create unique index IDX_TBL_USER_ROLE on TBL_CORE_USER_ROLE (USER_ID, ROLE_ID);

-- Create table
create table TBL_CORE_ROLE_MENU
(
    role_id VARCHAR2(32),
    menu_id VARCHAR2(32)
);
-- Add comments to the columns
comment on column TBL_CORE_ROLE_MENU.role_id
    is '角色id';
comment on column TBL_CORE_ROLE_MENU.menu_id
    is '菜单id';
-- Create/Recreate indexes
create unique index IDX_TBL_CORE_USER_ROLE on TBL_CORE_ROLE_MENU (ROLE_ID, MENU_ID);