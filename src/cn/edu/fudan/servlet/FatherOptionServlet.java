package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.UnitSearch;
import cn.edu.fudan.dao.FatherOptionDAO;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/20.
 */
@WebServlet(name = "FatherOptionServlet", urlPatterns = {"/admin/fatheroption"})
public class FatherOptionServlet extends BaseServlet<Void, List<UnitSearch>> {
    public FatherOptionServlet() {
        super(GET, Void.class);
    }

    @Override
    protected List<UnitSearch> processRequest(Void request, Session session) throws Exception {
        return new FatherOptionDAO(this).getResult();
    }
}
