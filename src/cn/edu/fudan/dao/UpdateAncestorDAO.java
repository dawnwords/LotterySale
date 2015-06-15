package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public abstract class UpdateAncestorDAO extends BaseDAO<Boolean> {
    public UpdateAncestorDAO(HttpServlet servlet) {
        super(servlet);
    }

    protected int fatherId(Connection connection, int unitId) throws SQLException {
        return intQueryFormTabUnitById(connection, "fatherid", unitId);
    }

    protected int level(Connection connection, int unitId) throws SQLException {
        return intQueryFormTabUnitById(connection, "level", unitId);
    }

    private int intQueryFormTabUnitById(Connection connection, String field, int unitId) throws SQLException {
        String sql = "SELECT " + field + " FROM tab_unit WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, unitId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }
}