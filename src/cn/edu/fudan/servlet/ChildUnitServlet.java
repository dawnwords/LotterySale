package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.Unit;
import cn.edu.fudan.dao.ChildUnitDAO;
import cn.edu.fudan.request.SingleUnitRequest;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/15.
 */
@WebServlet(name = "ChildUnitServlet", urlPatterns = "/cunit")
public class ChildUnitServlet extends BaseServlet<SingleUnitRequest, List<Unit>> {

    public ChildUnitServlet() {
        super(POST, SingleUnitRequest.class);
    }

    @Override
    protected List<Unit> processRequest(SingleUnitRequest request) throws Exception {
        String unitId = request == null ? "0" : String.valueOf(request.unitId);
        return new ChildUnitDAO(this, unitId).getResult();
    }

}
