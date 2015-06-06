package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.CompareUnitSale;
import cn.edu.fudan.dao.CompareSaleDAO;
import cn.edu.fudan.request.SingleUnitRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/5/17.
 */
@WebServlet(name = "CompareSaleServlet", urlPatterns = {"/comparesale"})
public class CompareSaleServlet extends BaseServlet<SingleUnitRequest, CompareUnitSale> {
    public CompareSaleServlet() {
        super(POST, SingleUnitRequest.class);
    }

    @Override
    protected CompareUnitSale processRequest(SingleUnitRequest request, Session session) throws Exception {
        return request == null ? null : new CompareSaleDAO(this, request.unitId).getResult();
    }
}
