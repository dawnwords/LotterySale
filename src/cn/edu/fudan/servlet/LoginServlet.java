package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.User;
import cn.edu.fudan.dao.LoginDAO;
import cn.edu.fudan.request.LoginRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/6.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends BaseServlet<LoginRequest, String> {
    public LoginServlet() {
        super(POST, LoginRequest.class);
    }

    @Override
    protected String processRequest(LoginRequest request, Session session) throws Exception {
        User user = new LoginDAO(this, request).getResult();
        session.user(user);
        return user == null ? "fail" : "success";
    }
}
