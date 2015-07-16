package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.YearDAO;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Dawnwords on 2015/7/15.
 */
@WebServlet(name = "YearServlet", urlPatterns = "/year")
public class YearServlet extends BaseServlet<Void, HashMap<String, ArrayList<String>>> {
    public YearServlet() {
        super(GET, Void.class);
    }

    @Override
    protected HashMap<String, ArrayList<String>> processRequest(Void request, Session session) throws Exception {
        return new YearDAO(this).getResult();
    }
}
