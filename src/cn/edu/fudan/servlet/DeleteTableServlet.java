package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.DeleteTableDAO;
import cn.edu.fudan.request.DeleteTableRequest;
import cn.edu.fudan.util.Session;

/**
 * Created by Dawnwords on 2015/6/18.
 */
public class DeleteTableServlet extends BaseServlet<DeleteTableRequest, String> {
    private DeleteTableRequest request;
    public DeleteTableServlet(DeleteTableRequest request) {
        super(POST, DeleteTableRequest.class);
        this.request = request;
    }

    @Override
    protected String processRequest(DeleteTableRequest request, Session session) throws Exception {
        return new DeleteTableDAO(this, request).getResult();
    }
}
