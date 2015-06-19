package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Dawnwords on 2015/6/19.
 */
public class RefreshTableDAO extends BaseDAO<Boolean> {

    private String table;

    public RefreshTableDAO(HttpServlet servlet, String table) {
        super(servlet);
        this.table = table;
    }

    @Override
    protected Boolean processData(Connection connection) throws Exception {
        try {
            Table table = Table.valueOf(this.table.toUpperCase());
            PreparedStatement ps = connection.prepareStatement(table.sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                table.updateDAOClass.getDeclaredConstructor(HttpServlet.class, int.class)
                        .newInstance(null, rs.getInt(1)).processData(connection);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private enum Table {
        UNIT("SELECT id FROM tab_unit WHERE level = 3 GROUP BY fatherid", UpdateAncestorUnitDAO.class),
        SALES("SELECT tab_sales.id FROM tab_sales " +
                "INNER JOIN tab_unit ON tab_sales.unitid = tab_unit.id " +
                "WHERE tab_unit.level = 3 " +
                "GROUP BY tab_unit.fatherid, tab_sales.saleyear, tab_sales.salemonth", UpdateAncestorSalesDAO.class);

        private String sql;
        private Class<? extends UpdateAncestorDAO> updateDAOClass;

        Table(String sql, Class<? extends UpdateAncestorDAO> updateDAOClass) {
            this.sql = sql;
            this.updateDAOClass = updateDAOClass;
        }
    }
}
