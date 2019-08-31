package com.cxp.jdbc;

import org.apache.ibatis.builder.BuilderException;
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
 * @create: 2019-08-31 08:45
 */
public class TestMybatisSql {

    public static void main(String[] args) throws IOException {
        singleParam();
    }

/*Connection connection = DBCPUtils.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            preparedStatement = connection.prepareStatement(boundSql.getSql());
            preparedStatement.setInt(1,Integer.parseInt(String.valueOf(boundSql.getParameterObject())));
            rs = preparedStatement.executeQuery();
            User user = null;
            List<User> list = new ArrayList<>();
            while (rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setAge(rs.getInt("age"));
                user.setUserName(rs.getString("user_name"));
                user.setName(rs.getString("name"));
                user.setBirthday(rs.getDate("birthday"));
                user.setSex(rs.getInt("sex"));
                user.setCreated(rs.getTimestamp("created"));
                user.setUpdated(rs.getTimestamp("updated"));
                list.add(user);
            }
            System.out.println(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                // 关闭连接，释放资源
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/

    protected <T> Class<? extends T> resolveClass(String alias,Configuration configuration) {
        if (alias == null) {
            return null;
        }
        try {
            return configuration.getTypeAliasRegistry().resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }

    public static void singleParam(){
        String sql = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
                "<select id=\"queryUserById\" resultType=\"java.util.Map\" parameterType=\"java.lang.Integer\">\n" +
                "        select * from tb_user where id = #{id}\n" +
                "    </select>";

        InputStream inputStream = new ByteArrayInputStream(sql.getBytes());

        XPathParser xPathParser = new XPathParser(inputStream);
        XNode xNode1 = xPathParser.evalNode("select");

        XMLLanguageDriver xmlLanguageDriver = new XMLLanguageDriver();
        Configuration configuration=new Configuration();
        DataSource dataSource = DBCPUtils.getDataSource();
        Environment environment = new Environment("test",new JdbcTransactionFactory(), dataSource);
        configuration.setEnvironment(environment);
        String resultType = xNode1.getStringAttribute("resultType");

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
        MappedStatement.Builder builder = new MappedStatement.Builder(configuration,"statement",sqlSource, SqlCommandType.SELECT);
        builder.resultMaps(resultMaps);
        MappedStatement mappedStatement = builder.build();

        try {
            List<Object> objects = executor.query(mappedStatement, 1, rowBounds, null);
            System.out.println(objects);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
