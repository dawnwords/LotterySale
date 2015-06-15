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

    private int id;

    protected UpdateAncestorDAO(HttpServlet servlet, int id) {
        super(servlet);
        this.id = id;
    }

    @Override
    protected Boolean processData(Connection connection) throws Exception {
        int unitId = unitId(connection, id);
        int level = level(connection, unitId);
        if (!assertLevel(level)) return false;

        int fatherId = unitId;
        while (true) {
            fatherId = fatherId(connection, fatherId);
            if (fatherId == 0) break;
            PreparedStatement ps = prepareStatement(connection, level);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
        return true;
    }

    protected abstract int unitId(Connection connection, int id) throws SQLException;

    protected abstract PreparedStatement prepareStatement(Connection connection, int level) throws SQLException;

    protected abstract boolean assertLevel(int level);

    private int fatherId(Connection connection, int unitId) throws SQLException {
        return intQueryFormTabUnitById(connection, "fatherid", unitId);
    }

    private int level(Connection connection, int unitId) throws SQLException {
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