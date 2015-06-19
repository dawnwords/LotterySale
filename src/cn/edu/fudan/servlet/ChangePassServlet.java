package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.User;
import cn.edu.fudan.dao.ChangePassDAO;
import cn.edu.fudan.request.ChangePassRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/8.
 */
@WebServlet(name = "ChangePassServlet", urlPatterns = {"/changepass"})
public class ChangePassServlet extends BaseServlet<ChangePassRequest, String> {
    public ChangePassServlet() {
        super(POST, ChangePassRequest.class);
    }

    @Override
    protected String processRequest(ChangePassRequest request, Session session) throws Exception {
        User user = session.user();
        if (user == null) return "fail";
        Boolean result = new ChangePassDAO(this, request, user.id).getResult();
        return (result != null && result) ? "success" : "fail";
    }
}
