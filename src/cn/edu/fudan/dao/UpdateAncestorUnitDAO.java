package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public class UpdateAncestorUnitDAO extends UpdateAncestorDAO {
    public UpdateAncestorUnitDAO(HttpServlet servlet, int unitId) {
        super(servlet, unitId);
    }

    @Override
    protected boolean assertLevel(int level) {
        return level == 2 || level == 3;
    }

    @Override
    protected void doFather(Connection connection, int level, int fatherId) throws SQLException {
        String sql = level == 3 ?
                "UPDATE tab_unit AS A CROSS JOIN (" +
                        "SELECT sum(B.unitnum) AS sumUnitnum," +
                        "       sum(B.population1) AS sumPopulation1," +
                        "       sum(B.population2) AS sumPopulation2 " +
                        "FROM tab_unit AS B " +
                        "WHERE B.fatherid = A.id" +
                        ") AS C " +
                        "SET A.unitnum = C.sumUnitnum," +
                        "    A.population1 = C.sumPopulation1," +
                        "    A.population2 = C.sumPopulation2 " +
                        "WHERE A.id = ?" :
                "UPDATE tab_unit AS A CROSS JOIN (" +
                        "SELECT sum(B.area) AS sumArea " +
                        "FROM tab_unit AS B " +
                        "WHERE B.fatherid = A.id" +
                        ") AS C " +
                        "SET A.area = C.sumArea " +
                        "WHERE A.id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, fatherId);
        ps.executeUpdate();
    }
}
