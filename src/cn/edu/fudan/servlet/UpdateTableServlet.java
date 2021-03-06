package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.UpdateTableDAO;
import cn.edu.fudan.request.UpdateTableRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/12.
 */
@WebServlet(name = "UpdateTableServlet", urlPatterns = {"/admin/updatetable"})
public class UpdateTableServlet extends BaseServlet<UpdateTableRequest, String> {
    public UpdateTableServlet() {
        super(POST, UpdateTableRequest.class);
    }

    @Override
    protected String processRequest(UpdateTableRequest request, Session session) throws Exception {
        return request == null || request.table() == UpdateTableRequest.Table.DEFAULT ? "error" :
                new UpdateTableDAO(this, request).getResult() ? "success" : "fail";
    }
}
