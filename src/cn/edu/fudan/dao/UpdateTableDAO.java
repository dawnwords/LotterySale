package cn.edu.fudan.dao;

import cn.edu.fudan.request.UpdateTableRequest;
import cn.edu.fudan.request.UpdateTableRequest.Update;
import cn.edu.fudan.util.Log;
import cn.edu.fudan.util.TypeUtil;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/12.
 */
public class UpdateTableDAO extends BaseDAO<Boolean> {
    private UpdateTableRequest request;

    public UpdateTableDAO(HttpServlet servlet, UpdateTableRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected Boolean processData(Connection connection) throws Exception {
        List<Update> updates = request.updates();
        String table = request.table().table;
        if (updates.size() > 0) {
            String sql = "UPDATE " + table + " SET ";
            for (Update update : updates) {
                sql += update.field() + "=?,";
            }
            sql = sql.substring(0, sql.length() - 1) + " WHERE valid = 1 AND id = ?";
            System.out.println(sql);

            PreparedStatement ps = connection.prepareStatement(sql);
            int i = 1;
            for (Update update : updates) {
                String type = update.type();
                PreparedStatement.class.getDeclaredMethod("set" + type, int.class, TypeUtil.string2Class(type))
                        .invoke(ps, i++, update.update());
            }
            ps.setInt(i, request.id());
            if (ps.executeUpdate() == 1) {
                Log.update(new Log.Parameter(connection, table, request.id()), updates);
                UnitFieldDAO.updateAncestor(connection, table, request.id());
                return true;
            }
        }
        return false;
    }
}
