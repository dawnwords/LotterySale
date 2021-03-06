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
                "sum(tab_sales.s1), sum(tab_sales.s2), sum(tab_sales.s3), sum(tab_sales.stotal), " +
                "tab_unit.name, tab_unit.population1, tab_unit.population2 " +
                "from tab_sales inner join tab_unit on tab_sales.unitid = tab_unit.id where " +
                "tab_unit.valid = 1 and tab_sales.valid = 1 and (";
        for (int ignored : unitids) {
            sql += "tab_unit.id = ? or ";
        }
        sql +=  "false) group by tab_sales.unitid, tab_sales.saleyear";

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
            result.add(new UnitSale(unitid, year.name, year.population1, year.population2,
                    year.data, quarter.data, month.data));
        }
        return result;
    }

    private HashMap<Integer, NameSaleData> executeSQL(Connection connection, Dimen dimen) throws Exception {
        PreparedStatement ps = connection.prepareStatement(sql + dimen.sql);
        int i = 1;
        for (int unitid : unitids) {
            ps.setInt(i++, unitid);
        }
        ResultSet rs = ps.executeQuery();
        HashMap<Integer, NameSaleData> result = new HashMap<>();
        while (rs.next()) {
            int unitid = rs.getInt(1);
            String year = rs.getString(2);
            String quarter = rs.getString(3);
            String month = rs.getString(4);
            double s1 = nullDouble(rs, 5);
            double s2 = nullDouble(rs, 6);
            double s3 = nullDouble(rs, 7);
            double stotal = nullDouble(rs, 8);

            if (!result.containsKey(unitid)) {
                String unitName = rs.getString(9);
                int population1 = rs.getInt(10);
                int population2 = rs.getInt(11);
                result.put(unitid, new NameSaleData(unitName, population1, population2));
            }
            String time = dimen.formatter.format(year, quarter, month);
            result.get(unitid).data.add(new SaleData(time, s1, s2, s3, stotal));
        }
        return result;
    }

    private class NameSaleData {
        String name;
        int population1, population2;
        List<SaleData> data;

        public NameSaleData(String name, int population1, int population2) {
            this.name = name;
            this.population1 = population1;
            this.population2 = population2;
            this.data = new ArrayList<>();
        }
    }

    private enum Dimen {
        YEAR("", new TimeFormatter() {
            @Override
            public String format(String year, String quarter, String month) {
                return year + "/01/01";
            }
        }),
        QUARTER(", tab_sales.salequarter", new TimeFormatter() {
            @Override
            public String format(String year, String quarter, String month) {
                return year + "/" + (Integer.parseInt(quarter) * 3) + "/01";
            }
        }),
        MONTH(", tab_sales.salemonth", new TimeFormatter() {
            @Override
            public String format(String year, String quarter, String month) {
                return year + "/" + month + "/01";
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
