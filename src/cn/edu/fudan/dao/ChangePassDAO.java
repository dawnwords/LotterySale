package cn.edu.fudan.dao;

import cn.edu.fudan.request.ChangePassRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Dawnwords on 2015/6/8.
 */
public class ChangePassDAO extends BaseDAO<Boolean> {
    private ChangePassRequest reqeust;
    private int userId;

    public ChangePassDAO(HttpServlet servlet, ChangePassRequest reqeust, int userId) {
        super(servlet);
        this.reqeust = reqeust;
        this.userId = userId;
    }

    @Override
    protected Boolean processData(Connection connection) throws Exception {
        String sql = "UPDATE tab_user SET password=password(?) WHERE id=? AND password=password(?) AND valid = 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, reqeust.newPass);
        ps.setInt(2, userId);
        ps.setString(3, reqeust.oldPass);
        return ps.executeUpdate() == 1;
    }
}
