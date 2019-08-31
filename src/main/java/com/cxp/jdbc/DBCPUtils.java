package com.cxp.jdbc;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @program: mybatis
 * @description:
 * @author: cheng
 * @create: 2019-08-31 10:47
 */
public class DBCPUtils {
    private static DataSource dataSource;
    static{
        try {
            //1.加载找properties文件输入流
            InputStream is = DBCPUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            //2.加载输入流
            Properties props = new Properties();
            props.load(is);
            //3.创建数据源
            dataSource = BasicDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource getDataSource(){
        return dataSource;
    }

    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
