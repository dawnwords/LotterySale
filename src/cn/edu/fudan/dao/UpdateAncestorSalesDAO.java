package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Dawnwords on 2015/6/15.
 */
public class UpdateAncestorSalesDAO extends BaseDAO<Boolean> {

    private int saleId;

    public UpdateAncestorSalesDAO(HttpServlet servlet, int saleId) {
        super(servlet);
        this.saleId = saleId;
    }

    @Override
    protected Boolean processData(Connection connection) throws Exception {
        String sql = "SELECT unitid,saleyear,salemonth,level FROM tab_sales " +
                "INNER JOIN tab_unit ON tab_unit.id = tab_sales.unitid WHERE tab_sales.id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, saleId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return false;

        int unitId = rs.getInt(1);
        String saleYear = rs.getString(2);
        String saleMonth = rs.getString(3);
        int level = rs.getInt(4);

        if (level != 3) return false;

        sql = "UPDATE tab_sales CROSS JOIN (" +
                "SELECT sum(B.s1) AS sumS1," +
                "       sum(B.s2) AS sumS2," +
                "       sum(B.s3) AS sumS3, " +
                "       sum(B.stotal) AS sumStotal " +
                "FROM tab_sales AS B INNER JOIN tab_unit ON B.unitid = tab_unit.id " +
                "WHERE tab_unit.valid = 1" +
                "   AND B.valid = 1" +
                "   AND tab_unit.fatherid = ?" +
                "   AND B.saleyear = ?" +
                "   AND B.salemonth = ?" +
                ") AS A " +
                "SET s1 = A.sumS1," +
                "    s2 = A.sumS2," +
                "    s3 = A.sumS3," +
                "    stotal = A.sumStotal " +
                "WHERE valid = 1" +
                "   AND unitid = ?" +
                "   AND saleyear = ?" +
                "   AND salemonth = ?";

        for (int fatherId = unitId; ; ) {
            fatherId = UnitFieldDAO.fatherId(connection, fatherId);
            if (fatherId <= 0) break;

            ps = connection.prepareStatement(sql);
            ps.setInt(1, fatherId);
            ps.setString(2, saleYear);
            ps.setString(3, saleMonth);
            ps.setInt(4, fatherId);
            ps.setString(5, saleYear);
            ps.setString(6, saleMonth);
            ps.executeUpdate();
        }
        return true;
    }
}
