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

        for (int fatherId = unitId; ; level--) {
            fatherId = fatherId(connection, fatherId);
            if (fatherId <= 0) break;

            String sql = level == 3 ?
                    "UPDATE tab_unit CROSS JOIN (" +
                            "SELECT sum(A.unitnum) AS sumUnitNum " +
                            "FROM tab_unit AS A " +
                            "WHERE A.valid = 1" +
                            "   AND A.fatherid = ?" +
                            ") AS B " +
                            "SET unitNum = B.sumUnitNum " +
                            "WHERE valid = 1" +
                            "   AND id = ?" :
                    "UPDATE tab_unit CROSS JOIN (" +
                            "SELECT round(sum(A.area),2) AS sumArea," +
                            "       sum(A.population1) AS sumPopulation1," +
                            "       sum(A.population2) AS sumPopulation2," +
                            "       sum(A.unitnum) AS sumUnitNum " +
                            "FROM tab_unit AS A " +
                            "WHERE A.valid = 1" +
                            "   AND A.fatherid = ?" +
                            ") AS B " +
                            "SET area = B.sumArea," +
                            "    population1 = B.sumPopulation1," +
                            "    population2 = B.sumPopulation2," +
                            "    unitnum = B.sumUnitNum " +
                            "WHERE valid = 1" +
                            "   AND id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, fatherId);
            ps.setInt(2, fatherId);
            ps.executeUpdate();
        }
        return true;
    }

}
