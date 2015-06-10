package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.TabUnit;
import cn.edu.fudan.dao.TabUnitDAO;
import cn.edu.fudan.request.TabUnitRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/6/10.
 */
@WebServlet(name = "TabUnitServlet", urlPatterns = {"/admin/tabunit"})
public class TabUnitServlet extends BaseServlet<TabUnitRequest, TabUnit> {
    public TabUnitServlet() {
        super(POST, TabUnitRequest.class);
    }

    @Override
    protected TabUnit processRequest(TabUnitRequest request, Session session) throws Exception {
        return new TabUnitDAO(this, request).getResult();
    }
}
