package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dawnwords on 2015/7/15.
 */
public class YearDAO extends BaseDAO<HashMap<String, ArrayList<String>>> {
    public YearDAO(HttpServlet servlet) {
        super(servlet);
    }

    @Override
    protected HashMap<String, ArrayList<String>> processData(Connection connection) throws Exception {
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        String sql = "SELECT DISTINCT saleyear,salemonth FROM tab_sales";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        rs.next();
        while (rs.next()) {
            String year = rs.getString(1);
            String month = rs.getString(2);
            ArrayList<String> months = result.get(year);
            if (months == null) {
                months = new ArrayList<>();
                result.put(year, months);
            }
            months.add(month);
        }
        return result;
    }
}
