package cn.edu.fudan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public class UnitFieldDAO {

    private static int queryIntField(Connection connection, String field, int unitId) throws SQLException {
        String sql = "SELECT " + field + " FROM tab_unit WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, unitId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? (int) rs.getObject(1) : 0;
    }

    static int saleLevel(Connection connection, int saleId) throws Exception {
        String sql = "SELECT level FROM tab_unit INNER JOIN tab_sales ON unitid=tab_unit.id WHERE tab_sales.id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, saleId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    static int level(Connection connection, int unitId) throws Exception {
        return queryIntField(connection, "level", unitId);
    }

    static int fatherId(Connection connection, int unitId) throws Exception {
        return queryIntField(connection, "fatherid", unitId);
    }

    static boolean updateAncestor(Connection connection, String table, int id) throws Exception {
        if (table.contains("sales"))
            return new UpdateAncestorSalesDAO(null, id).processData(connection);
        if (table.contains("unit"))
            return new UpdateAncestorUnitDAO(null, id).processData(connection);
        return false;
    }
}