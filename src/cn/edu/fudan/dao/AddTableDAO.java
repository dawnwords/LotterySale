package cn.edu.fudan.dao;

import cn.edu.fudan.request.AddTableRequest;
import cn.edu.fudan.request.AddTableRequest.Add;
import cn.edu.fudan.util.Log;
import cn.edu.fudan.util.TypeUtil;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/20.
 */
public class AddTableDAO extends BaseDAO<Integer> {
    private AddTableRequest request;

    public AddTableDAO(HttpServlet servlet, AddTableRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected Integer processData(Connection connection) throws Exception {
        String table = request.table().table;
        int level = UnitFieldDAO.level(connection, request.id());
        List<Add> adds = request.adds(level);

        if (adds.size() > 0) {
            String sql = "INSERT INTO " + table + "(";
            String qm = "";
            for (Add add : adds) {
                sql += add.field() + ",";
                qm += add.qm() + ",";
            }
            sql += "valid) VALUES (" + qm + "1)";

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            int i = 1;
            for (Add add : adds) {
                String type = add.type();
                PreparedStatement.class.getDeclaredMethod("set" + type, int.class, TypeUtil.string2Class(type))
                        .invoke(ps, i++, add.value());
            }
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int autoId = rs.getInt(1);
                    Log.add(new Log.Parameter(connection, table, autoId));
                    UnitFieldDAO.updateAncestor(connection, table, autoId);
                    return autoId;
                }
            }
        }
        return -1;
    }

}
