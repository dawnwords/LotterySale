package cn.edu.fudan.servlet;

import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/7.
 */
@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends BaseServlet<Void, Void> {
    public LogoutServlet() {
        super(GET | POST, Void.class);
    }

    @Override
    protected Void processRequest(Void request, Session session) throws Exception {
        session.user(null);
        session.redirect("/index.jsp");
        return null;
    }
}
