package com.cxp.jdbc;

import com.cxp.mybatis.pojo.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: mybatis
 * @description:
 * @author: cheng
 * @create: 2019-08-29 18:46
 */
public class JDBC {

    public static void main(String[] args) {
        Connection connection = null;
        PreparedStatement pst= null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://192.168.153.128:3306/springboot2-project?characterEncoding=utf-8&useSSL=false";
            String username = "root";
            String password = "123456";
            connection = DriverManager.getConnection(url, username, password);
            pst= connection.prepareStatement("select * from tb_user where id = ? ");
            //设置参数
            pst.setInt(1,1);
            rs = pst.executeQuery();
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                // 关闭连接，释放资源
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
