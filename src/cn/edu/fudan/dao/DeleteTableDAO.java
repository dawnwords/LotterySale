package cn.edu.fudan.dao;

import cn.edu.fudan.request.DeleteTableRequest;
import cn.edu.fudan.util.Log;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Dawnwords on 2015/6/18.
 */
public class DeleteTableDAO extends BaseDAO<String> {
    private DeleteTableRequest request;

    public DeleteTableDAO(HttpServlet servlet, DeleteTableRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected String processData(Connection connection) throws Exception {
        String sql;
        switch (request.table()) {
            case UNIT:
                sql = "UPDATE tab_unit CROSS JOIN (" +
                        "SELECT count(id) AS childnum FROM tab_unit WHERE valid = 1 AND fatherid = ?) AS T " +
                        "SET valid = 0 " +
                        "WHERE id = ? AND T.childnum = 0";
                break;
            case SALES:
                sql = "UPDATE tab_sales CROSS JOIN(" +
                        "(SELECT count(tab_sales.id) AS childnum FROM tab_sales " +
                        "   INNER JOIN tab_unit ON tab_sales.unitid = tab_unit.id" +
                        "   INNER JOIN tab_sales AS T ON T.unitid = tab_unit.fatherid" +
                        "   AND T.salemonth = tab_sales.salemonth" +
                        "   AND T.saleyear = tab_sales.saleyear" +
                        "   WHERE tab_sales.valid = 1 AND T.id = ?)) AS P " +
                        "SET valid = 0 WHERE id = ? AND P.childnum = 0";
                break;
            case USER:
                sql = "UPDATE tab_user SET valid = 0 WHERE id = ? OR id = ?";
                break;
            default:
                return "error";
        }
        int id = request.id();
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setInt(2, id);
        if (ps.executeUpdate() == 1) {
            String table = request.table().table;
            Log.delete(new Log.Parameter(connection, table, id));
            UnitFieldDAO.updateAncestor(connection, table, id);
            return "success";
        }
        return "fail";

    }
}
