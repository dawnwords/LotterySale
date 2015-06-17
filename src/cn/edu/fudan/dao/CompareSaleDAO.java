package cn.edu.fudan.dao;

import cn.edu.fudan.bean.CompareUnitSale;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Dawnwords on 2015/5/17.
 */
public class CompareSaleDAO extends BaseDAO<CompareUnitSale> {

    private int unitId;

    public CompareSaleDAO(HttpServlet servlet, int unitId) {
        super(servlet);
        this.unitId = unitId;
    }

    @Override
    protected CompareUnitSale processData(Connection connection) throws Exception {
        String sql = "SELECT tab_sales.unitid, tab_unit.name, tab_unit.population1, tab_unit.population2, " +
                "tab_sales.saleyear, tab_sales.salemonth, tab_sales.s1, tab_sales.s2, tab_sales.s3, tab_sales.stotal " +
                "FROM tab_sales INNER JOIN tab_unit ON tab_sales.unitid = tab_unit.id " +
                "WHERE tab_sales.unitid = ? AND tab_unit.valid = 1 AND tab_sales.valid = 1 " +
                "ORDER BY tab_sales.saleyear, tab_sales.salemonth";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, unitId);
        ResultSet rs = ps.executeQuery();
        CompareUnitSale result = null;
        while (rs.next()) {
            if (result == null) {
                int unitid = rs.getInt(1);
                String unitName = rs.getString(2);
                int population1 = rs.getInt(3);
                int population2 = rs.getInt(4);
                result = new CompareUnitSale(unitid, unitName, population1, population2);
            }
            String year = rs.getString(5);
            String month = rs.getString(6);
            double s1 = nullDouble(rs, 7);
            double s2 = nullDouble(rs, 8);
            double s3 = nullDouble(rs, 9);
            double stotal = nullDouble(rs, 10);
            result.pushData(year, month, s1, s2, s3, stotal);
        }
        return result;
    }
}
