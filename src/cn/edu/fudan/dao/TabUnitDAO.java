package cn.edu.fudan.dao;

import cn.edu.fudan.bean.TabUnit;
import cn.edu.fudan.request.TabUnitRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Dawnwords on 2015/6/10.
 */
public class TabUnitDAO extends BaseDAO<TabUnit> {
    private TabUnitRequest request;
    private static final String[] COLS = {"id", "name", "unitcode", "address", "manager", "mobile", "unitnum", "area", "population1", "population2"};
    private static final int[] SEARCH_COLS = {1, 3, 4};

    public TabUnitDAO(HttpServlet servlet, TabUnitRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected TabUnit processData(Connection connection) throws Exception {
        TabUnit result = new TabUnit();
        result.draw(request.draw());
        result.recordsTotal(getTotal(connection));
        result.recordsFiltered(getFiltered(connection));

        ResultSet rs = preparedStatement(connection).executeQuery();
        while (rs.next()) {
            ArrayList data = new ArrayList();
            data.add(rs.getInt(1));
            data.add(rs.getString(2));
            data.add(rs.getString(3));
            data.add(rs.getString(4));
            data.add(rs.getString(5));
            data.add(rs.getString(6));
            data.add(rs.getInt(7));
            data.add(rs.getDouble(8));
            data.add(rs.getInt(9));
            data.add(rs.getInt(10));
            result.addData(data);
        }
        return result;
    }

    private PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(prepareSQL());

        int i = 1;
        if (request.search() != null) {
            for (; i <= SEARCH_COLS.length; i++) {
                ps.setString(i, "%" + request.search() + "%");
            }
        }
        ps.setInt(i++, request.start());
        ps.setInt(i, request.length());
        return ps;
    }

    private String prepareSQL() {
        String sql = "SELECT ";
        for (String col : COLS) {
            sql += col + ",";
        }
        sql = removeComma(sql) + " FROM tab_unit ";
        if (request.search() != null && SEARCH_COLS.length > 0) {
            sql += "where ";
            for (int col : SEARCH_COLS) {
                sql += COLS[col] + " like ? or ";
            }
            sql += "false ";
        }
        if (request.order().size() > 0) {
            sql += "order by ";
            for (TabUnitRequest.Order order : request.order()) {
                sql += COLS[order.column()] + " " + order.dir() + ",";
            }
            sql = removeComma(sql);
        }
        sql += " LIMIT ?,?";

        System.out.println(sql);
        return sql;
    }

    private String removeComma(String s) {
        return s.substring(0, s.length() - 1);
    }

    private int getFiltered(Connection connection) throws SQLException {
        int filtered = 0;
        String sql = "SELECT COUNT(id) FROM tab_unit ";
        if (request.search() != null && SEARCH_COLS.length > 0) {
            sql += "where ";
            for (int col : SEARCH_COLS) {
                sql += COLS[col] + " like ? or ";
            }
            sql += "false";
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        if (request.search() != null) {
            for (int i = 1; i <= SEARCH_COLS.length; i++) {
                ps.setString(i, "%" + request.search() + "%");
            }
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            filtered = rs.getInt(1);
        }
        return filtered;
    }

    private int getTotal(Connection connection) throws SQLException {
        int total = 0;
        String sql = "SELECT count(id) FROM tab_unit";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }
        return total;
    }

}
