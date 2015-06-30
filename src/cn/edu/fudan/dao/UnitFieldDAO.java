package cn.edu.fudan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public class UnitFieldDAO {

    private static int queryIntField(Connection connection, String sql, int unitId) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, unitId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? (int) rs.getObject(1) : -1;
    }

    static int saleLevel(Connection connection, int saleId) throws Exception {
        return queryIntField(connection, "SELECT level FROM tab_unit INNER JOIN tab_sales ON unitid=tab_unit.id WHERE tab_sales.id = ?", saleId);
    }

    static int level(Connection connection, int unitId) throws Exception {
        return queryIntField(connection, "SELECT level FROM tab_unit WHERE id = ?", unitId);
    }

    static int fatherId(Connection connection, int unitId) throws Exception {
        return queryIntField(connection, "SELECT fatherid FROM tab_unit WHERE id = ?", unitId);
    }

    static boolean updateAncestor(Connection connection, String table, int id) throws Exception {
        if (table.contains("sales"))
            return new UpdateAncestorSalesDAO(null, id).processData(connection);
        if (table.contains("unit"))
            return new UpdateAncestorUnitDAO(null, id).processData(connection);
        return false;
    }

    public interface LevelGetter {
        int level(Connection connection, int id);

        LevelGetter NullLevelGetter = new LevelGetter() {
            @Override
            public int level(Connection connection, int id) {
                return -1;
            }
        };

        LevelGetter SaleLevelGetter = new LevelGetter() {
            @Override
            public int level(Connection connection, int id) {
                try {
                    return saleLevel(connection, id);
                } catch (Exception e) {
                    return -1;
                }
            }
        };

        LevelGetter UnitLevelGetter = new LevelGetter() {
            @Override
            public int level(Connection connection, int id) {
                try {
                    return level(connection, id);
                } catch (Exception e) {
                    return -1;
                }
            }
        };
    }

}