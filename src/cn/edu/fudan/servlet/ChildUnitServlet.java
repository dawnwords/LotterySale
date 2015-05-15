package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.Unit;
import cn.edu.fudan.dao.ChildUnitDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/15.
 */
@WebServlet(name = "ChildUnitServlet", urlPatterns = "/cunit")
public class ChildUnitServlet extends BaseServlet<List<Unit>> {

    public ChildUnitServlet() {
        super(GET);
    }

    @Override
    protected List<Unit> processRequest(HttpServletRequest request) throws Exception {
        String unitId = request.getParameter("unitId");
        unitId = unitId == null || "".equals(unitId) ? "0" : unitId;
        return new ChildUnitDAO(this, unitId).getResult();
    }
}
