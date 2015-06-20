package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public class UnitFieldDAO<T> extends BaseDAO<T> {

    private String field;
    private int unitId;

    private UnitFieldDAO(String field, int unitId) {
        this(null, field, unitId);
    }

    public UnitFieldDAO(HttpServlet servlet, String field, int unitId) {
        super(servlet);
        this.field = field;
        this.unitId = unitId;
    }

    @Override
    protected T processData(Connection connection) throws Exception {
        String sql = "SELECT " + field + " FROM tab_unit WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, unitId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? (T) rs.getObject(1) : null;
    }

    static int level(Connection connection, int unitId) throws Exception {
        Integer level = new UnitFieldDAO<Integer>("level", unitId).processData(connection);
        return level == null ? 0 : level;
    }

    static int fatherId(Connection connection, int unitId) throws Exception {
        Integer fatherId = new UnitFieldDAO<Integer>("fatherId", unitId).processData(connection);
        return fatherId == null ? 0 : fatherId;
    }
}