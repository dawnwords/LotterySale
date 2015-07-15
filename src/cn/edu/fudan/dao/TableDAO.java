package cn.edu.fudan.dao;

import cn.edu.fudan.bean.Field;
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
            int i = 1;
            for (Field field : table.fields) {
                data.add(ResultSet.class.getDeclaredMethod("get" + field.type(), int.class).invoke(rs, i++));
            }
            result.addData(data);
        }
        return result;
    }

    private PreparedStatement preparedStatement(Connection connection) throws SQLException {
        String sql = "SELECT ";
        for (Field field : table.fields) {
            sql += field.name() + ",";
        }
        sql = removeComma(sql) + " FROM " + table.table + " where valid = 1 " + searchSQL();
        if (request.order().size() > 0) {
            sql += "order by ";
            for (TableRequest.Order order : request.order()) {
                sql += table.fields[order.column()].name() + " " + order.dir() + ",";
            }
            sql = removeComma(sql);
        }
        sql += " LIMIT ?,?";
        PreparedStatement ps = connection.prepareStatement(sql);
        int i = prepareSearch(ps);
        ps.setInt(i++, request.start());
        ps.setInt(i, request.length());
        return ps;
    }

    private String removeComma(String s) {
        return s.substring(0, s.length() - 1);
    }

    private int getFiltered(Connection connection) throws SQLException {
        int filtered = 0;
        String sql = "SELECT COUNT(id) FROM " + table.table + " where valid = 1 " + searchSQL();
        PreparedStatement ps = connection.prepareStatement(sql);
        if (request.search() != null) {
            prepareSearch(ps);
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            filtered = rs.getInt(1);
        }
        return filtered;
    }

    private String searchSQL() {
        String sql = "";
        if (request.search() != null) {
            sql += "AND (";
            for (Field field : table.fields) {
                if (field.isSearch()) {
                    sql += field.name() + " like ? or ";
                }
            }
            sql += "false)";
        }
        return sql;
    }

    private int prepareSearch(PreparedStatement ps) throws SQLException {
        int i = 1;
        if (request.search() != null) {
            for (Field field : table.fields) {
                if (field.isSearch()) {
                    ps.setString(i++, "%" + request.search() + "%");
                }
            }
        }
        return i;
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
