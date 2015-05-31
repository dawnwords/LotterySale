package cn.edu.fudan.dao;

import cn.edu.fudan.bean.UnitSearch;
import cn.edu.fudan.request.SearchUnitRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/31.
 */
public class SearchUnitDAO extends BaseDAO<List<UnitSearch>> {
    private SearchUnitRequest request;

    public SearchUnitDAO(HttpServlet servlet, SearchUnitRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected List<UnitSearch> processData(Connection connection) throws Exception {
        ArrayList<UnitSearch> result = new ArrayList<>();
        if (request != null) {
            String sql = "SELECT id,name FROM tab_unit WHERE name LIKE ? LIMIT ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "%" + request.keyword + "%");
            ps.setInt(2, request.count);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                result.add(new UnitSearch(id, name));
            }
        }
        return result;
    }
}
