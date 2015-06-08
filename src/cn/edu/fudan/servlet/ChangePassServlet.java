package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.User;
import cn.edu.fudan.dao.ChangePassDAO;
import cn.edu.fudan.request.ChangePassRequest;
import cn.edu.fudan.util.Session;

/**
 * Created by Dawnwords on 2015/6/8.
 */
public class ChangePassServlet extends BaseServlet<ChangePassRequest, String> {
    public ChangePassServlet() {
        super(POST, ChangePassRequest.class);
    }

    @Override
    protected String processRequest(ChangePassRequest request, Session session) throws Exception {
        User user = session.user();
        boolean success = user != null && new ChangePassDAO(this, request, user.id).getResult();
        return success ? "success" : "fail";
    }
}
