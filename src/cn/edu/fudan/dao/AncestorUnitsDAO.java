package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/1.
 */
public class AncestorUnitsDAO extends BaseDAO<List<Integer>> {
    private int unitId;

    public AncestorUnitsDAO(HttpServlet servlet, int unitId) {
        super(servlet);
        this.unitId = unitId;
    }

    @Override
    protected List<Integer> processData(Connection connection) throws Exception {
        int fatherId = unitId;
        LinkedList<Integer> result = new LinkedList<>();
        String sql = "SELECT fatherid FROM tab_unit WHERE id = ? AND valid = 1";
        PreparedStatement ps = connection.prepareStatement(sql);
        while (fatherId != 0) {
            ps.setInt(1, fatherId);
            ResultSet rs = ps.executeQuery();
            fatherId = rs.next() ? rs.getInt(1) : 0;
            if (fatherId != 0) result.addFirst(fatherId);
        }
        return result;
    }
}
