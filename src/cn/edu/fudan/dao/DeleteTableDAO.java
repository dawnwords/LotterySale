package cn.edu.fudan.dao;

import cn.edu.fudan.request.DeleteTableRequest;

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
                sql = "UPDATE tab_unit SET valid = 0 WHERE id = ? AND (SELECT count(id) FROM tab_unit WHERE fatherid = ?) = 0";
                break;
            case SALES:
                sql = "UPDATE tab_sales SET valid = 0 WHERE id = ? AND (SELECT count(tab_sales.id) FROM tab_sales " +
                        "INNER JOIN tab_unit ON tab_sales.unitid = tab_unit.id " +
                        "INNER JOIN tab_sales AS T ON T.unitid = tab_unit.fatherid " +
                        "AND T.salemonth = tab_sales.salemonth " +
                        "AND T.saleyear = tab_sales.saleyear " +
                        "WHERE T.id = ?) = 0";
                break;
            case USER:
                sql = "UPDATE tab_user SET valid = 0 WHERE id = ? OR id = ?";
                break;
            default:
                return "error";
        }
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, request.id());
        ps.setInt(2, request.id());
        return ps.executeUpdate() == 1 ? "success" : "fail";

    }
}
