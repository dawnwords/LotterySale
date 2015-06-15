package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public class UpdateAncestorSalesDAO extends UpdateAncestorDAO {
    private String saleYear;
    private String saleMonth;

    public UpdateAncestorSalesDAO(HttpServlet servlet, int unitId, String saleYear, String saleMonth) {
        super(servlet, unitId);
        this.saleYear = saleYear;
        this.saleMonth = saleMonth;
    }

    @Override
    protected boolean assertLevel(int level) {
        return level == 3;
    }

    @Override
    protected void doFather(Connection connection, int level, int fatherId) throws SQLException {
        String sql = "UPDATE tab_sales AS A CROSS JOIN (" +
                "SELECT sum(B.s1) AS sumS1," +
                "       sum(B.s2) AS sumS2," +
                "       sum(B.s3) AS sumS3, " +
                "       sum(B.stotal) AS sumStotal " +
                "FROM tab_sales AS B " +
                "       INNER JOIN tab_unit AS D ON B.unitid = D.id " +
                "WHERE D.fatherId = A.unitId " +
                "       AND B.saleyear = A.saleyear" +
                "       AND B.salemonth = A.salemonth" +
                ") AS C " +
                "SET A.s1 = C.sumS1," +
                "    A.s2 = C.sumS2," +
                "    A.s3 = C.sumS3," +
                "    A.stotal = C.sumStotal " +
                "WHERE A.id = ? AND A.saleyear = ? AND A.salemonth = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, fatherId);
        ps.setString(2, saleYear);
        ps.setString(3, saleMonth);
        ps.executeUpdate();
    }
}
