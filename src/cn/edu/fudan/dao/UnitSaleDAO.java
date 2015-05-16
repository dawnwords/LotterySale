package cn.edu.fudan.dao;

import cn.edu.fudan.bean.SaleData;
import cn.edu.fudan.bean.UnitSale;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/16.
 */
public class UnitSaleDAO extends BaseDAO<List<UnitSale>> {
    private int[] unitids;
    private String sql;

    public UnitSaleDAO(HttpServlet servlet, int[] unitids) {
        super(servlet);
        this.unitids = unitids;
        sql = "select tab_sales.unitid, tab_sales.saleyear, tab_sales.salequarter, tab_sales.salemonth, " +
                "sum(tab_sales.s1), sum(tab_sales.s2), sum(tab_sales.s3), sum(tab_sales.stotal), tab_unit.name " +
                "from tab_sales inner join tab_unit on tab_sales.unitid = tab_unit.id where tab_unit.level = 3 and (";
        for (int unitid : unitids) {
            sql += "tab_unit.id = ? or tab_unit.fatherid = ? or ";
        }
        sql = sql.substring(0, sql.length() - 4) + ") group by tab_sales.unitid, tab_sales.saleyear";

    }

    @Override
    protected List<UnitSale> processData(Connection connection) throws Exception {
        HashMap<Integer, NameSaleData> yearData = executeSQL(connection, Dimen.YEAR);
        HashMap<Integer, NameSaleData> quarterData = executeSQL(connection, Dimen.QUARTER);
        HashMap<Integer, NameSaleData> monthData = executeSQL(connection, Dimen.MONTH);

        List<UnitSale> result = new ArrayList<>();
        for (int unitid : yearData.keySet()) {
            NameSaleData year = yearData.get(unitid);
            NameSaleData quarter = quarterData.get(unitid);
            NameSaleData month = monthData.get(unitid);
            result.add(new UnitSale(unitid, year.name, year.data, quarter.data, month.data));
        }
        return result;
    }

    private HashMap<Integer, NameSaleData> executeSQL(Connection connection, Dimen dimen) throws Exception {
        PreparedStatement ps = connection.prepareStatement(sql + dimen.sql);
        int i = 1;
        for (int unitid : unitids) {
            ps.setInt(i++, unitid);
            ps.setInt(i++, unitid);
        }
        ResultSet rs = ps.executeQuery();
        HashMap<Integer, NameSaleData> result = new HashMap<>();
        while (rs.next()) {
            int unitid = rs.getInt(1);
            String year = rs.getString(2);
            String quarter = rs.getString(3);
            String month = rs.getString(4);
            double s1 = rs.getDouble(5);
            double s2 = rs.getDouble(6);
            double s3 = rs.getDouble(7);
            double stotal = rs.getDouble(8);

            if (!result.containsKey(unitid)) {
                String unitName = rs.getString(9);
                result.put(unitid, new NameSaleData(unitName));
            }
            String time = dimen.formatter.format(year, quarter, month);
            result.get(unitid).data.add(new SaleData(time, s1, s2, s3, stotal));
        }
        return result;
    }

    private class NameSaleData{
        String name;
        List<SaleData> data;

        public NameSaleData(String name) {
            this.name = name;
            this.data = new ArrayList<>();
        }
    }

    private enum Dimen {
        YEAR("", new TimeFormatter() {
            @Override
            public String format(String year, String quarter, String month) {
                return year + "年";
            }
        }),
        QUARTER(", tab_sales.salequarter", new TimeFormatter() {
            @Override
            public String format(String year, String quarter, String month) {
                return year + "年第" + quarter + "季度";
            }
        }),
        MONTH(", tab_sales.salemonth", new TimeFormatter() {
            @Override
            public String format(String year, String quarter, String month) {
                return year + "/" + month;
            }
        });
        protected String sql;
        protected TimeFormatter formatter;

        Dimen(String sql, TimeFormatter formatter) {
            this.sql = sql;
            this.formatter = formatter;
        }
    }

    private interface TimeFormatter {
        String format(String year, String quarter, String month);
    }
}
