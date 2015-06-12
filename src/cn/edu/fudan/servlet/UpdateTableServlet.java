package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.UpdateTableDAO;
import cn.edu.fudan.request.UpdateTableReqeust;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/12.
 */
@WebServlet(name = "UpdateTableServlet", urlPatterns = {"/admin/updatetable"})
public class UpdateTableServlet extends BaseServlet<UpdateTableReqeust, String> {
    public UpdateTableServlet() {
        super(POST, UpdateTableReqeust.class);
    }

    @Override
    protected String processRequest(UpdateTableReqeust request, Session session) throws Exception {
        return new UpdateTableDAO(this, request).getResult();
    }
}
