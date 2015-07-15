package cn.edu.fudan.dao;

import cn.edu.fudan.bean.Field;
import cn.edu.fudan.bean.TableResponse;
import cn.edu.fudan.request.SaleWarningRequest;
import cn.edu.fudan.request.TableRequest.Order;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Dawnwords on 2015/7/15.
 */
public class SaleWarningDAO extends BaseDAO<TableResponse> {
    public static final String SQL = "FROM tab_unit INNER JOIN (" +
            "SELECT presale.unitid AS unitid, presale.stotal AS prestotal, thissale.stotal AS thisstotal, " +
            "((thissale.stotal - presale.stotal) / presale.stotal) AS ratio " +
            "FROM tab_sales AS presale INNER JOIN tab_sales AS thissale ON thissale.unitid = presale.unitid " +
            "WHERE thissale.saleyear = ? AND thissale.salemonth = ? AND presale.saleyear = ? " +
            "AND presale.salemonth = ? AND thissale.valid = 1 AND presale.valid = 1" +
            ") AS sale ON id = unitid WHERE (ratio >= 0.3 OR ratio <= -0.3) AND valid = 1";
    private SaleWarningRequest request;

    public SaleWarningDAO(HttpServlet servlet, SaleWarningRequest request) {
        super(servlet);
        this.request = request;
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
            for (Field field : SaleWarningRequest.FIELDS) {
                data.add(ResultSet.class.getDeclaredMethod("get" + field.type(), int.class).invoke(rs, i++));
            }
            result.addData(data);
        }

        return result;
    }

    private PreparedStatement preparedStatement(Connection connection) throws Exception {
        String sql = "SELECT id, name, address, prestotal, thisstotal, ratio " + SQLSearch();
        if (request.order().size() > 0) {
            sql += " ORDER BY ";
            for (Order order : request.order()) {
                sql += SaleWarningRequest.FIELDS[order.column()].name() + " " + order.dir() + ",";
            }
            sql = removeComma(sql);
        }
        sql += " LIMIT ?,?";
        PreparedStatement ps = connection.prepareStatement(sql);
        int i = prepareSearch(ps);
        ps.setInt(i, request.start());
        ps.setInt(i + 1, request.length());
        return ps;
    }

    private String removeComma(String s) {
        return s.substring(0, s.length() - 1);
    }

    private int getFiltered(Connection connection) throws SQLException {
        int filtered = 0;
        String sql = "SELECT COUNT(id) " + SQLSearch();
        PreparedStatement ps = connection.prepareStatement(sql);
        prepareSearch(ps);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            filtered = rs.getInt(1);
        }
        return filtered;
    }

    private int getTotal(Connection connection) throws SQLException {
        int total = 0;
        String sql = "SELECT count(id) " + SQL;
        PreparedStatement ps = connection.prepareStatement(sql);
        prepareYearMonth(ps);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            total = rs.getInt(1);
        }
        return total;
    }

    private String SQLSearch() {
        String sql = SQL;
        if (request.search() != null) {
            sql += " AND (";
            for (Field field : SaleWarningRequest.FIELDS) {
                if (field.isSearch()) {
                    sql += field.name() + " like ? or ";
                }
            }
            sql += "false)";
        }
        return sql;
    }

    private int prepareSearch(PreparedStatement ps) throws SQLException {
        int i = prepareYearMonth(ps);
        if (request.search() != null) {
            for (Field field : SaleWarningRequest.FIELDS) {
                if (field.isSearch()) {
                    ps.setString(i++, "%" + request.search() + "%");
                }
            }
        }
        return i;
    }

    private int prepareYearMonth(PreparedStatement ps) throws SQLException {
        int i = 1;
        ps.setString(i++, request.year());
        ps.setString(i++, request.month());
        ps.setString(i++, request.preYear());
        ps.setString(i++, request.preMonth());
        return i;
    }
}
