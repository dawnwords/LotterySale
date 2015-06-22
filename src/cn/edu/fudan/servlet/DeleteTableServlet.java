package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.DeleteTableDAO;
import cn.edu.fudan.request.DeleteTableRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/18.
 */
@WebServlet(name = "DeleteTableServlet", urlPatterns = {"/admin/deletetable"})
public class DeleteTableServlet extends BaseServlet<DeleteTableRequest, String> {

    public DeleteTableServlet() {
        super(POST, DeleteTableRequest.class);
    }

    @Override
    protected String processRequest(DeleteTableRequest request, Session session) throws Exception {
        return request == null || request.table() == DeleteTableRequest.Table.DEFAULT ? "error"
                : new DeleteTableDAO(this, request).getResult();
    }
}
