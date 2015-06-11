package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.TableResponse;
import cn.edu.fudan.dao.TableDAO;
import cn.edu.fudan.request.TableRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/10.
 */
@WebServlet(name = "TableServlet", urlPatterns = {"/admin/table"})
public class TableServlet extends BaseServlet<TableRequest, TableResponse> {
    public TableServlet() {
        super(POST, TableRequest.class);
    }

    @Override
    protected TableResponse processRequest(TableRequest request, Session session) throws Exception {
        return request.table() == TableRequest.Table.DEFAULT ? null : new TableDAO(this, request).getResult();
    }
}
