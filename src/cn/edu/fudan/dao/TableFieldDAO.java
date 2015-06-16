package cn.edu.fudan.dao;

import cn.edu.fudan.bean.TableField;
import cn.edu.fudan.request.TableFieldRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dawnwords on 2015/6/16.
 */
public class TableFieldDAO extends BaseDAO<TableField[]> {
    private TableFieldRequest request;

    private static final TableField[] SALES_LV3 = {new TableField("id", false), new TableField("unitid", false),
            new TableField("saleyear", false), new TableField("salequater", false), new TableField("salemonth", false),
            new TableField("s1", true), new TableField("s2", false), new TableField("s3", true), new TableField("stotal", true)};
    private static final TableField[] SALES_OTHER = {new TableField("id", false), new TableField("unitid", false),
            new TableField("saleyear", false), new TableField("salequater", false), new TableField("salemonth", false),
            new TableField("s1", false), new TableField("s2", false), new TableField("s3", false), new TableField("stotal", false)};
    private static final TableField[] UNIT_LV2 = {new TableField("id", false), new TableField("name", true),
            new TableField("unicode", true), new TableField("address", true), new TableField("manager", true),
            new TableField("mobile", true), new TableField("unitnum", false), new TableField("area", true),
            new TableField("population1", true), new TableField("population2", true)};
    private static final TableField[] UNIT_OTHER = {new TableField("id", false), new TableField("name", true),
            new TableField("unicode", true), new TableField("address", true), new TableField("manager", true),
            new TableField("mobile", true), new TableField("unitnum", false), new TableField("area", false),
            new TableField("population1", false), new TableField("population2", false)};

    public TableFieldDAO(HttpServlet servlet, TableFieldRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected TableField[] processData(Connection connection) throws Exception {
        int level = level(connection);
        if (level < 0) return null;
        if ("sales".equals(request.table)) {
            return level == 3 ? SALES_LV3 : SALES_OTHER;
        }
        if ("unit".equals(request.table)) {
            return level == 2 ? UNIT_LV2 : UNIT_OTHER;
        }
        return null;
    }

    private int level(Connection connection) throws SQLException {
        String sql = "SELECT level FROM tab_unit WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, request.unitid);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : -1;
    }
}
