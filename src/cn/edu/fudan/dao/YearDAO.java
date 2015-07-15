package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/7/15.
 */
public class YearDAO extends BaseDAO<List<String>> {
    public YearDAO(HttpServlet servlet) {
        super(servlet);
    }

    @Override
    protected List<String> processData(Connection connection) throws Exception {
        List<String> result = new ArrayList<>();
        String sql = "SELECT DISTINCT saleyear FROM tab_sales";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.add(rs.getString(1));
        }
        return result;
    }
}
