-- Create table
CREATE TABLE TBL_CORE_USER
(
    id              VARCHAR2(32)  NOT NULL,
    user_name       VARCHAR2(100) NOT NULL,
    pass_word       VARCHAR2(200) NOT NULL,
    nick_name       VARCHAR2(100) NOT NULL,
    tel             VARCHAR2(20),
    mail            VARCHAR2(30),
    last_login_time DATE,
    last_login_ip   VARCHAR2(30),
    portrait        clob
);
-- Add comments to the columns
COMMENT ON COLUMN TBL_CORE_USER.id IS '主键';
COMMENT ON COLUMN TBL_CORE_USER.user_name IS '用户名';
COMMENT ON COLUMN TBL_CORE_USER.pass_word IS '密码';
COMMENT ON COLUMN TBL_CORE_USER.nick_name IS '昵称';
COMMENT ON COLUMN TBL_CORE_USER.tel IS '电话';
COMMENT ON COLUMN TBL_CORE_USER.mail IS '邮箱';
COMMENT ON COLUMN TBL_CORE_USER.last_login_time IS '上次登陆时间';
COMMENT ON COLUMN TBL_CORE_USER.last_login_ip IS '上次登陆ip';
COMMENT ON COLUMN TBL_CORE_USER.portrait IS '头像';
-- Create/Recreate indexes
CREATE UNIQUE INDEX PK_TBL_CORE_USER_1 ON TBL_CORE_USER (USER_NAME);
CREATE INDEX PK_TBL_CORE_USER_2 ON TBL_CORE_USER (USER_NAME, PASS_WORD);
-- Create/Recreate primary, unique and foreign key constraints
ALTER TABLE TBL_CORE_USER
    ADD CONSTRAINT PK_TBL_CORE_USER PRIMARY KEY (id);

-- Create table
CREATE TABLE TBL_CORE_MENU
(
    id         VARCHAR2(32) NOT NULL,
    menu_name  VARCHAR2(30) NOT NULL,
    parent_id  VARCHAR2(32) NOT NULL,
    permission VARCHAR2(60),
    type       VARCHAR2(1)  NOT NULL,
    url        VARCHAR2(100),
    icon       VARCHAR2(30),
    mlevel     NUMBER(1)    NOT NULL,
    is_leaf    VARCHAR2(1)  NOT NULL
);
-- Add comments to the columns
COMMENT ON COLUMN TBL_CORE_MENU.id IS 'id';
COMMENT ON COLUMN TBL_CORE_MENU.menu_name IS '名称';
COMMENT ON COLUMN TBL_CORE_MENU.parent_id IS '父级id';
COMMENT ON COLUMN TBL_CORE_MENU.permission IS '权限标识';
COMMENT ON COLUMN TBL_CORE_MENU.type IS '类型，0菜单1按钮';
COMMENT ON COLUMN TBL_CORE_MENU.url IS '访问地址';
COMMENT ON COLUMN TBL_CORE_MENU.icon IS '图标fa字体';
COMMENT ON COLUMN TBL_CORE_MENU.mlevel IS '等级';
COMMENT ON COLUMN TBL_CORE_MENU.is_leaf IS '是否底级';
-- Create/Recreate primary, unique and foreign key constraints
ALTER TABLE TBL_CORE_MENU
    ADD CONSTRAINT PK_TBL_CORE_MENU PRIMARY KEY (id);

-- Create table
CREATE TABLE TBL_CORE_ROLE
(
    id        VARCHAR2(32) NOT NULL,
    role_name VARCHAR2(60) NOT NULL,
    permission varchar(1)
);
-- Add comments to the columns
COMMENT ON COLUMN TBL_CORE_ROLE.id IS 'id';
COMMENT ON COLUMN TBL_CORE_ROLE.role_name IS '角色名称';
COMMENT ON COLUMN TBL_CORE_ROLE.permission IS '权限';
-- Create/Recreate primary, unique and foreign key constraints
ALTER TABLE TBL_CORE_ROLE
    ADD CONSTRAINT PK_TBL_CORE_ROLE PRIMARY KEY (id);
CREATE UNIQUE INDEX IDX_TBL_CORE_ROLE_1 ON TBL_CORE_ROLE (ROLE_NAME);
CREATE TABLE TBL_CORE_ROLE_USER
(
    user_id VARCHAR2(32),
    role_id VARCHAR2(32)
);
-- Add comments to the columns
COMMENT ON COLUMN TBL_CORE_ROLE_USER.user_id IS '用户id';
COMMENT ON COLUMN TBL_CORE_ROLE_USER.role_id IS '角色id';
CREATE UNIQUE INDEX IDX_TBL_ROLE_USER ON TBL_CORE_ROLE_USER (USER_ID, ROLE_ID);

-- Create table
CREATE TABLE TBL_CORE_ROLE_MENU
(
    role_id VARCHAR2(32),
    menu_id VARCHAR2(32)
);
-- Add comments to the columns
COMMENT ON COLUMN TBL_CORE_ROLE_MENU.role_id IS '角色id';
COMMENT ON COLUMN TBL_CORE_ROLE_MENU.menu_id IS '菜单id';
-- Create/Recreate indexes
CREATE UNIQUE INDEX IDX_TBL_CORE_ROLE_MENU ON TBL_CORE_ROLE_MENU (ROLE_ID, MENU_ID);

-- Create table
CREATE TABLE TBL_CORE_AGENCY
(
    id          VARCHAR2(32)  NOT NULL,
    agency_code VARCHAR2(15)  NOT NULL,
    agency_name VARCHAR2(120) NOT NULL,
    type        VARCHAR2(1)   NOT NULL,
    parent_id   VARCHAR2(32)  NOT NULL,
    mlevel      NUMBER(1)     NOT NULL,
    is_leaf     VARCHAR2(1)
) TABLESPACE USERS PCTFREE 10 INITRANS 1 MAXTRANS 255;
-- Add comments to the table
COMMENT ON TABLE TBL_CORE_AGENCY IS '单位表';
-- Add comments to the columns
COMMENT ON COLUMN TBL_CORE_AGENCY.id IS 'id';
COMMENT ON COLUMN TBL_CORE_AGENCY.agency_code IS '单位编码';
COMMENT ON COLUMN TBL_CORE_AGENCY.agency_name IS '单位名称';
COMMENT ON COLUMN TBL_CORE_AGENCY.type IS '种类，1单位，2部门';
COMMENT ON COLUMN TBL_CORE_AGENCY.parent_id IS '父菜单';
COMMENT ON COLUMN TBL_CORE_AGENCY.mlevel IS '等级';
COMMENT ON COLUMN TBL_CORE_AGENCY.is_leaf IS '是否底级';
-- Create/Recreate indexes
CREATE UNIQUE INDEX IDX_TBL_CORE_AGENCY ON TBL_CORE_AGENCY (AGENCY_CODE);
-- Create/Recreate primary, unique and foreign key constraints
ALTER TABLE TBL_CORE_AGENCY
    ADD CONSTRAINT TBL_CORE_AGENCY_PK PRIMARY KEY (ID);

-- Create table
CREATE TABLE TBL_CORE_ROLE_AGENCY
(
    agency_id VARCHAR2(32),
    role_id   VARCHAR2(32)
);
-- Add comments to the columns
COMMENT ON COLUMN TBL_CORE_ROLE_AGENCY.agency_id IS '单位id';
COMMENT ON COLUMN TBL_CORE_ROLE_AGENCY.role_id IS '角色id';
-- Create/Recreate indexes
CREATE UNIQUE INDEX IDX_TBL_ROLE_AGENCY ON TBL_CORE_ROLE_AGENCY (AGENCY_ID, ROLE_ID);

-- Create table
CREATE TABLE tbl_core_log
(
    id            varchar2(32),
    oper_model    varchar2(32),
    oper_type     varchar2(32),
    oper_desc     varchar2(300),
    oper_method   varchar2(300),
    oper_return   clob,
    oper_userid   varchar2(60),
    oper_username varchar2(300),
    oper_ip       varchar2(30),
    oper_uri      varchar2(300),
    oper_time     date,
    oper_param    clob
)
;
-- Add comments to the columns
COMMENT ON COLUMN tbl_core_log.id IS '主键';
COMMENT ON COLUMN tbl_core_log.oper_model IS '模块';
COMMENT ON COLUMN tbl_core_log.oper_type IS '类型';
COMMENT ON COLUMN tbl_core_log.oper_desc IS '描述';
COMMENT ON COLUMN tbl_core_log.oper_method IS '方法';
COMMENT ON COLUMN tbl_core_log.oper_return IS '返回';
COMMENT ON COLUMN tbl_core_log.oper_userid IS '用户ID';
COMMENT ON COLUMN tbl_core_log.oper_username IS '用户名';
COMMENT ON COLUMN tbl_core_log.oper_ip IS 'ip';
COMMENT ON COLUMN tbl_core_log.oper_uri IS '链接';
COMMENT ON COLUMN tbl_core_log.oper_time IS '时间';
-- Create/Recreate primary, unique and foreign key constraints
ALTER TABLE tbl_core_log
    ADD CONSTRAINT pk_tbl_core_log PRIMARY KEY (ID);


-- Create sequence
CREATE SEQUENCE seq_agency_code MINVALUE 1 MAXVALUE 9999999 START WITH 1 INCREMENT BY 1 CACHE 10;
