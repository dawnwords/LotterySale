package cn.edu.fudan.util;

import cn.edu.fudan.request.UpdateTableRequest.Update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/14.
 */
public class Log {
    public static void update(Parameter p, List<Update> updates) throws SQLException {
        String content = "";
        for (Update update : updates) {
            content += String.format("%s:%s->%s,", update.field(), update.origin(), update.update());
        }
        log(p, Operation.MODIFY, content.substring(0, content.length() - 1));
    }

    public static void delete(Parameter p) throws SQLException {
        log(p, Operation.DELETE, "");
    }

    public static void add(Parameter p) throws SQLException {
        log(p, Operation.ADD, "");
    }

    private static void log(Parameter p, Operation operation, String content) throws SQLException {
        String sql = "INSERT INTO tab_log(`date`, operation, content, `table`,dataId) VALUES (NOW(),?,?,?,?)";
        PreparedStatement ps = p.connection.prepareStatement(sql);
        ps.setString(1, operation.name());
        ps.setString(2, content);
        ps.setString(3, p.table);
        ps.setInt(4, p.id);
        ps.execute();
    }

    private enum Operation {
        ADD, DELETE, MODIFY
    }

    public static class Parameter {
        public final Connection connection;
        public final String table;
        public final int id;

        public Parameter(Connection connection, String table, int id) {
            this.id = id;
            this.table = table;
            this.connection = connection;
        }
    }

}
