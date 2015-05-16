package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.Unit;
import cn.edu.fudan.dao.ChildUnitDAO;
import cn.edu.fudan.request.CunitRequest;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/15.
 */
@WebServlet(name = "ChildUnitServlet", urlPatterns = "/cunit")
public class ChildUnitServlet extends BaseServlet<CunitRequest, List<Unit>> {

    public ChildUnitServlet() {
        super(POST, CunitRequest.class);
    }

    @Override
    protected List<Unit> processRequest(CunitRequest request) throws Exception {
        String unitId = request == null ? "0" : String.valueOf(request.unitId);
        return new ChildUnitDAO(this, unitId).getResult();
    }

}
