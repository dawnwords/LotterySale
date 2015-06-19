package cn.edu.fudan.dao;

import cn.edu.fudan.bean.TableResponse;
import cn.edu.fudan.request.TableRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Dawnwords on 2015/6/11.
 */
public class TableDAO extends BaseDAO<TableResponse> {

    private TableRequest request;
    private TableRequest.Table table;

    public TableDAO(HttpServlet servlet, TableRequest request) {
        super(servlet);
        this.request = request;
        this.table = request.table();
    }

    @Override
    protected TableResponse processData(Connection connection) throws Exception {
        TableResponse result = new TableResponse();
        result.draw(request.draw());
        result.recordsTotal(getTotal(connection));
        result.recordsFiltered(getFiltered(connection));

        ResultSet rs = preparedStatement(connection).executeQuery();
        while (rs.next()) {
            ArrayList data = new ArrayList();
            for (int i = 0; i < table.cols.length; i++) {
                data.add(ResultSet.class.getDeclaredMethod("get" + table.colTypes[i], int.class).invoke(rs, i + 1));
            }
            result.addData(data);
        }
        return result;
    }

    private PreparedStatement preparedStatement(Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(prepareSQL());

        int i = 1;
        if (request.search() != null) {
            for (; i <= table.searchCols.length; i++) {
                ps.setString(i, "%" + request.search() + "%");
            }
        }
        ps.setInt(i++, request.start());
        ps.setInt(i, request.length());
        return ps;
    }

    private String prepareSQL() {
        String sql = "SELECT ";
        for (String col : table.cols) {
            sql += col + ",";
        }
        sql = removeComma(sql) + " FROM " + table.table + " where valid = 1 ";
        if (request.search() != null && table.searchCols.length > 0) {
            sql += "AND (";
            for (int col : table.searchCols) {
                sql += table.cols[col] + " like ? or ";
            }
            sql += "false) ";
        }
        if (request.order().size() > 0) {
            sql += "order by ";
            for (TableRequest.Order order : request.order()) {
                sql += table.cols[order.column()] + " " + order.dir() + ",";
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
        String sql = "SELECT COUNT(id) FROM " + table.table + " where valid = 1";
        if (request.search() != null && table.searchCols.length > 0) {
            sql += "AND (";
            for (int col : table.searchCols) {
                sql += table.cols[col] + " like ? or ";
            }
            sql += "false)";
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        if (request.search() != null) {
            for (int i = 1; i <= table.searchCols.length; i++) {
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
        String sql = "SELECT count(id) FROM " + table.table + " WHERE valid = 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }
        return total;
    }
}
