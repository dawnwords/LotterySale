package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.AddTableDAO;
import cn.edu.fudan.dao.UpdateAncestorSalesDAO;
import cn.edu.fudan.dao.UpdateAncestorUnitDAO;
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
        if (request == null || request.table() == AddTableRequest.Table.DEFAULT) return "error";

        int id = new AddTableDAO(this, request).getResult();
        if (id > 0) {
            switch (request.table()) {
                case SALES:
                    new UpdateAncestorSalesDAO(this, id).getResult();
                    break;
                case UNIT:
                    new UpdateAncestorUnitDAO(this, id).getResult();
                    break;
            }
            return "success";
        }
        return "fail";
    }
}
