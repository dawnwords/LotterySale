package cn.edu.fudan.dao;

import cn.edu.fudan.bean.User;
import cn.edu.fudan.request.LoginRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Dawnwords on 2015/6/6.
 */
public class LoginDAO extends BaseDAO<User> {
    private LoginRequest request;

    public LoginDAO(HttpServlet servlet, LoginRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected User processData(Connection connection) throws Exception {
        String sql = "SELECT id,name,authority FROM tab_user WHERE name=? AND password=password(?) AND valid = 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, request.name);
        ps.setString(2, request.password);
        ResultSet rs = ps.executeQuery();
        User user = null;
        if (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String authority = rs.getString(3);
            user = new User(id, authority, name);
        }
        return user;
    }
}
