package cn.edu.fudan.dao;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/1.
 */
public class AncestorUnitsDAO extends BaseDAO<List<Integer>> {
    private int unitId;

    public AncestorUnitsDAO(HttpServlet servlet, int unitId) {
        super(servlet);
        this.unitId = unitId;
    }

    @Override
    protected List<Integer> processData(Connection connection) throws Exception {
        LinkedList<Integer> result = new LinkedList<>();
        for (int fatherId = unitId; ; ) {
            fatherId = UnitFieldDAO.fatherId(connection, fatherId);
            if (fatherId == 0) break;
            result.addFirst(fatherId);
        }
        return result;
    }
}
