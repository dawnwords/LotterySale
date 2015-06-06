package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.UnitSale;
import cn.edu.fudan.dao.UnitSaleDAO;
import cn.edu.fudan.request.MultiUnitRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/16.
 */
@WebServlet(name = "SaleServlet", urlPatterns = {"/sale"})
public class SaleServlet extends BaseServlet<MultiUnitRequest, List<UnitSale>> {

    public SaleServlet() {
        super(POST, MultiUnitRequest.class);
    }

    @Override
    protected List<UnitSale> processRequest(MultiUnitRequest request, Session session) throws Exception {
        int[] unitids = request.unitId;
        log(Arrays.toString(unitids));
        return unitids.length == 0 ? new ArrayList<UnitSale>() : new UnitSaleDAO(this, unitids).getResult();
    }
}
