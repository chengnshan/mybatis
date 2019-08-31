package com.cxp.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

/**
 * @program: mybatis
 * @description:
 * @author: cheng
 * @create: 2019-08-30 21:43
 */
@Intercepts({@Signature(type= Executor.class,method = "update,query",args = {MappedStatement.class})})
public class MybatisInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        return invocation.proceed();
    }
}
