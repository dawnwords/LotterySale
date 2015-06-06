package cn.edu.fudan.servlet;

import cn.edu.fudan.bean.UnitSearch;
import cn.edu.fudan.dao.SearchUnitDAO;
import cn.edu.fudan.request.SearchUnitRequest;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebServlet;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/31.
 */
@WebServlet(name = "SearchUnitServlet", urlPatterns = "/sunit")
public class SearchUnitServlet extends BaseServlet<SearchUnitRequest, List<UnitSearch>> {
    public SearchUnitServlet() {
        super(POST, SearchUnitRequest.class);
    }

    @Override
    protected List<UnitSearch> processRequest(SearchUnitRequest request, Session session) throws Exception {
        return new SearchUnitDAO(this, request).getResult();
    }
}
