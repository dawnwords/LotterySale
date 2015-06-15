package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public class UpdateAncestorUnitDAO extends UpdateAncestorDAO {

    private int unitId;

    public UpdateAncestorUnitDAO(HttpServlet servlet, int unitId) {
        super(servlet);
        this.unitId = unitId;
    }

    @Override
    protected Boolean processData(Connection connection) throws Exception {
        int level = level(connection, unitId);
        if (level != 2 && level != 3) return false;

        int fatherId = unitId;
        while (true) {
            fatherId = fatherId(connection, fatherId);
            if (fatherId == 0) break;
            String sql = level == 3 ?
                    "UPDATE tab_unit CROSS JOIN (" +
                            "SELECT sum(A.unitnum) AS sumUnitnum," +
                            "       sum(A.population1) AS sumPopulation1," +
                            "       sum(A.population2) AS sumPopulation2 " +
                            "FROM tab_unit AS A " +
                            "WHERE A.fatherid = tab_unit.id" +
                            ") AS B " +
                            "SET unitnum = B.sumUnitnum," +
                            "    population1 = B.sumPopulation1," +
                            "    population2 = B.sumPopulation2 " +
                            "WHERE tab_unit.id = ?" :
                    "UPDATE tab_unit CROSS JOIN (" +
                            "SELECT sum(A.area) AS sumArea " +
                            "FROM tab_unit AS A " +
                            "WHERE A.fatherid = id" +
                            ") AS B " +
                            "SET area = B.sumArea " +
                            "WHERE tab_unit.id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, fatherId);
            ps.executeUpdate();
        }
        return true;
    }

}
