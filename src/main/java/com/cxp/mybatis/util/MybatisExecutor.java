package com.cxp.mybatis.util;

import com.cxp.jdbc.DBCPUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: mybatis
 * @description:
 * @author: cheng
 * @create: 2019-08-31 16:11
 */
public class MybatisExecutor {

    public static void main(String[] args) {
        String sql = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<select id=\"queryUserByMap\" resultType=\"java.util.Map\" parameterType=\"java.util.Map\">\n" +
                "        select * from tb_user " +
                "<where>" +
                "       <if test=\" name != null and name !=''\"> and name = #{name} </if>" +
                "       <if test=\" age != null \"> and age = #{age} </if>" +
                "</where> \n" +
                "    </select>";

        Map<String,Object> param = new HashMap<>();
        //   param.put("name","鹏程");
        param.put("age",22);

        List<Object> objects = executor(sql, param);
        if (objects != null){
            System.out.println(objects);
        }
    }

    public static List<Object> executor(String sql, Map<String,Object> param){
        InputStream inputStream = new ByteArrayInputStream(sql.getBytes());

        XPathParser xPathParser = new XPathParser(inputStream);
        XNode xNode1 = xPathParser.evalNode("select");

        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        Configuration configuration=new Configuration();
        DataSource dataSource = DBCPUtils.getDataSource();
        Environment environment = new Environment("test",new JdbcTransactionFactory(), dataSource);
        configuration.setEnvironment(environment);
        String resultType = xNode1.getStringAttribute("resultType");
        String statementId = xNode1.getStringAttribute("id");

        List<ResultMap> resultMaps = new ArrayList<>();
        Class<Object> objectClass = configuration.getTypeAliasRegistry().resolveAlias(resultType);
        ResultMap inlineResultMap = new ResultMap.Builder(
                configuration,
                "statement-Inline",
                objectClass,
                new ArrayList<>(),
                null).build();
        resultMaps.add(inlineResultMap);

        SqlSource sqlSource = xmlLanguageDriver.createSqlSource(configuration, xNode1, null);

        RowBounds rowBounds = new RowBounds(0,5000);
        Executor executor = new SimpleExecutor(configuration,
                configuration.getEnvironment().getTransactionFactory().newTransaction(dataSource,null,false));
        MappedStatement.Builder builder = new MappedStatement.Builder(configuration,statementId,sqlSource, SqlCommandType.SELECT);
        builder.resultMaps(resultMaps);
        MappedStatement mappedStatement = builder.build();

        try {
            List<Object> objects = executor.query(mappedStatement, param, rowBounds, null);
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
