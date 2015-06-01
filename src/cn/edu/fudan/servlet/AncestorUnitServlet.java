package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.AncestorUnitsDAO;
import cn.edu.fudan.request.SingleUnitRequest;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/1.
 */
@WebServlet(name = "AncestorUnitServlet", urlPatterns = "/aunit")
public class AncestorUnitServlet extends BaseServlet<SingleUnitRequest, List<Integer>> {
    public AncestorUnitServlet() {
        super(POST, SingleUnitRequest.class);
    }

    @Override
    protected List<Integer> processRequest(SingleUnitRequest request) throws Exception {
        return new AncestorUnitsDAO(this, request.unitId).getResult();
    }
}
