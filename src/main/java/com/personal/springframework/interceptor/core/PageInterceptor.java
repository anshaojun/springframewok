package com.personal.springframework.interceptor.core;

import com.personal.springframework.constant.RoleOptions;
import com.personal.springframework.model.Role;
import com.personal.springframework.model.User;
import com.personal.springframework.model.core.Page;
import com.personal.springframework.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @program: springframework
 * @description: 分页拦截器
 * @author: 安少军
 * @create: 2021-12-29 09:57
 **/
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Slf4j
@Component
@PropertySource("classpath:application-c3p0.properties")
public class PageInterceptor implements Interceptor {
    @Value("${c3p0.type}")
    private String jdbcType;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        //从RoutingStatementHandler中获得处理对象PreparedStatementHandler,从这个对象中获取Mapper中的xml信息
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        Object sqlCommandType = metaObject.getValue("delegate.mappedStatement.sqlCommandType");
        if (sqlCommandType.equals(SqlCommandType.SELECT)) {
            //配置文件中sql语句的id
            String id = mappedStatement.getId();
            //判断是否是需要分页的方法
            if (id.matches(".+ByPage$")) {
                //获得sql的参数信息
                BoundSql boundSql = statementHandler.getBoundSql();
                Page page = (Page) boundSql.getParameterObject();
                //原始的sql
                String sql = boundSql.getSql();
                //组装page中的信息,查询总条数
                String countSql = "select count(*) from (" + sql + ") a";
                Connection connection = (Connection) invocation.getArgs()[0];
                PreparedStatement statement = connection.prepareStatement(countSql);
                ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
                parameterHandler.setParameters(statement);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    int total = rs.getInt(1);
                    page.setTotalNum(total);
                    log.info("totalNum:{}", total);
                } else {
                    log.info("count is 0");
                }
                page.count();
                String pageSql = sql;
                //改造后的sql语句
                switch (jdbcType.toLowerCase()) {
                    case "oracle":
                        pageSql = "select * " +
                                "  from (select rownum as row_, a.* " +
                                " from (" +
                                sql +
                                ") a) " +
                                "  where row_> " + page.getStartRow() + " and row_<=" + page.getEndRow() + "";
                        break;
                    case "mysql":
                    case "postgresql":
                        pageSql = "select * " +
                                " from ( " +
                                sql +
                                " ) " +
                                " limit " + page.getStartRow() + "," + page.getEndRow();
                        break;
                    default:
                        break;
                }
                metaObject.setValue("delegate.boundSql.sql", pageSql);
            }
        }
        //放行
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
