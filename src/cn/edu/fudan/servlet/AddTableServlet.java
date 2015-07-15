package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.AddTableDAO;
import cn.edu.fudan.request.AddTableRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/20.
 */
@WebServlet(name = "AddTableServlet", urlPatterns = {"/admin/addtable"})
public class AddTableServlet extends BaseServlet<AddTableRequest, String> {
    public AddTableServlet() {
        super(POST, AddTableRequest.class);
    }

    @Override
    protected String processRequest(AddTableRequest request, Session session) throws Exception {
        if (request == null || request.table() == AddTableRequest.Table.DEFAULT)
            return "error";
        Integer result = new AddTableDAO(this, request).getResult();
        return (result != null && result > 0) ? "success" : "fail";
    }
}
