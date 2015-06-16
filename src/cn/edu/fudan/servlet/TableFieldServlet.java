package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.TableField;
import cn.edu.fudan.dao.TableFieldDAO;
import cn.edu.fudan.request.TableFieldRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/16.
 */
@WebServlet(name = "TableFieldServlet", urlPatterns = "/admin/tablefield")
public class TableFieldServlet extends BaseServlet<TableFieldRequest, TableField[]> {
    public TableFieldServlet() {
        super(POST, TableFieldRequest.class);
    }

    @Override
    protected TableField[] processRequest(TableFieldRequest request, Session session) throws Exception {
        return new TableFieldDAO(this, request).getResult();
    }
}
