package cn.edu.fudan.dao;

import cn.edu.fudan.request.UpdateTableRequest;
import cn.edu.fudan.request.UpdateTableRequest.Table;
import cn.edu.fudan.request.UpdateTableRequest.Update;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/12.
 */
public class UpdateTableDAO extends BaseDAO<String> {
    private UpdateTableRequest request;

    public UpdateTableDAO(HttpServlet servlet, UpdateTableRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected String processData(Connection connection) throws Exception {
        List<Update> updates = request.updates();
        Table table = request.table();
        if (updates.size() > 0) {
            String sql = "UPDATE " + table.table + " SET ";
            for (Update update : updates) {
                sql += update.field + "=?,";
            }
            sql = sql.substring(0, sql.length() - 1) + " WHERE id = ?";
            System.out.println(sql);

            PreparedStatement ps = connection.prepareStatement(sql);
            int i = 1;
            for (Update update : updates) {
                String type = table.colTypes[i - 1];
                PreparedStatement.class.getDeclaredMethod("set" + type, getClassBytype(type)).invoke(ps, update.update);
                i++;
            }
            ps.setInt(i, request.id());
            if (ps.executeUpdate() == 1) {
                return "修改成功";
            }
        }
        return "数据未修改";
    }

    private Class getClassBytype(String type) {
        if ("String".equals(type)) return String.class;
        if ("Int".equals(type)) return int.class;
        if ("Double".equals(type)) return double.class;
        return null;
    }
}