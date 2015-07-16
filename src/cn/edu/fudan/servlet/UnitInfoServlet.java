package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.UnitInfo;
import cn.edu.fudan.dao.UnitInfoDAO;
import cn.edu.fudan.request.SingleUnitRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Dawnwords on 2015/7/16.
 */
@WebServlet(name = "UnitInfoServlet", urlPatterns = {"/unitinfo"})
public class UnitInfoServlet extends BaseServlet<SingleUnitRequest, UnitInfo> {
    public UnitInfoServlet() {
        super(POST, SingleUnitRequest.class);
    }

    @Override
    protected UnitInfo processRequest(SingleUnitRequest request, Session session) throws Exception {
        return request == null || request.unitId < 0 ? null : new UnitInfoDAO(this, request.unitId).getResult();
    }
}
