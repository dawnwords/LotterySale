package cn.edu.fudan.dao;

import cn.edu.fudan.bean.UnitSearch;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/20.
 */
public class FatherOptionDAO extends BaseDAO<List<UnitSearch>> {

    public FatherOptionDAO(HttpServlet servlet) {
        super(servlet);
    }

    @Override
    protected List<UnitSearch> processData(Connection connection) throws Exception {
        List<UnitSearch> result = new ArrayList<>();
        String sql = "SELECT id,name,level FROM tab_unit WHERE valid = 1 AND fatherid = ?";
        processData( connection.prepareStatement(sql), 0, result);
        return result;
    }

    private void processData(PreparedStatement ps, int fatherid, List<UnitSearch> result) throws Exception {
        ps.setInt(1, fatherid);
        ResultSet rs = ps.executeQuery();
        List<UnitSearch> temp = new LinkedList<>();
        while (rs.next()) {
            int id = rs.getInt(1);
            String name = rs.getString(2);
            int level = rs.getInt(3);
            temp.add(new UnitSearch(id, name, level));
        }
        for (UnitSearch us : temp) {
            result.add(us);
            if (us.level() < 2) {
                processData(ps, us.id(), result);
            }
        }
    }
}
