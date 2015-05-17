package cn.edu.fudan.dao;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dawnwords on 2015/5/15.
 */
public abstract class BaseDAO<T> {

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ServletContext context;

    public BaseDAO(HttpServlet servlet) {
        this.context = servlet.getServletContext();
    }

    private Connection connect() throws SQLException {
        String host = context.getInitParameter("DBHost");
        String port = context.getInitParameter("DBPort");
        String db = context.getInitParameter("DBName");
        String user = context.getInitParameter("DBUser");
        String pass = context.getInitParameter("DBPass");
        String url = String.format("jdbc:mysql://%s:%s/%s", host, port, db);
        return DriverManager.getConnection(url, user, pass);
    }

    public T getResult() {
        Connection conn = null;
        T result = null;

        try {
            conn = connect();
            result = processData(conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    protected abstract T processData(Connection connection) throws Exception;

    protected double nullDouble(ResultSet rs, int columnIndex) throws SQLException {
        Double d = (Double)rs.getObject(columnIndex);
        return d == null?-1:d;
    }
}
