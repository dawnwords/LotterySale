package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.RefreshTableDAO;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/19.
 */
@WebServlet(name = "RefreshTableServlet", urlPatterns = {"/admin/refreshtable"})
public class RefreshTableServlet extends BaseServlet<String, String> {
    public RefreshTableServlet() {
        super(POST, String.class);
    }

    @Override
    protected String processRequest(String table, Session session) throws Exception {
        return new RefreshTableDAO(this, table).getResult() ? "success" : "fail";
    }
}
