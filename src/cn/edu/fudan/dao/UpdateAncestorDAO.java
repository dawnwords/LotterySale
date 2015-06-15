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
    private int unitId;

    protected UpdateAncestorDAO(HttpServlet servlet, int unitId) {
        super(servlet);
        this.unitId = unitId;
    }

    @Override
    protected Boolean processData(Connection connection) throws Exception {
        int level = level(connection, unitId);
        if (!assertLevel(level)) return false;

        int fatherId = unitId;
        while (true) {
            fatherId = fatherId(connection, fatherId);
            if (fatherId == 0) break;
            doFather(connection, level, fatherId);
        }
        return true;
    }

    protected abstract void doFather(Connection connection, int level, int fatherId) throws SQLException;

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