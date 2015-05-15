package cn.edu.fudan.dao;

import cn.edu.fudan.bean.Unit;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/15.
 */
public class ChildUnitDAO extends BaseDAO<List<Unit>> {
    private String unitId;

    public ChildUnitDAO(HttpServlet servlet, String unitId) {
        super(servlet);
        this.unitId = unitId;
    }

    @Override
    protected List<Unit> processData(Connection connection) throws Exception {
        List<Unit> result = new ArrayList<>();
        String sql = "select id,name,level from tab_unit where fatherid =?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, unitId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String id = String.valueOf(rs.getInt(1));
            String name = rs.getString(2);
            int level = rs.getInt(3);
            result.add(new Unit(id, name, level));
        }
        return result;
    }
}
