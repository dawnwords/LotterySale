package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.TableResponse;
import cn.edu.fudan.dao.SaleWarningDAO;
import cn.edu.fudan.request.SaleWarningRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/7/15.
 */
@WebServlet(name = "SaleWarningServlet", urlPatterns = {"/wsale"})
public class SaleWarningServlet extends BaseServlet<SaleWarningRequest, TableResponse> {
    public SaleWarningServlet() {
        super(POST, SaleWarningRequest.class);
    }

    @Override
    protected TableResponse processRequest(SaleWarningRequest request, Session session) throws Exception {
        return request == null ? null : new SaleWarningDAO(this, request).getResult();
    }
}
