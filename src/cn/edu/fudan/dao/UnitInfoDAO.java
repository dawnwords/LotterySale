package cn.edu.fudan.dao;

import cn.edu.fudan.bean.UnitInfo;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by Dawnwords on 2015/7/16.
 */
public class UnitInfoDAO extends BaseDAO<UnitInfo> {
    private int unitid;

    public UnitInfoDAO(HttpServlet servlet, int unitid) {
        super(servlet);
        this.unitid = unitid;
    }

    @Override
    protected UnitInfo processData(Connection connection) throws Exception {
        String sql = "SELECT id,name,unitcode,address,manager,mobile,latitude,longitude FROM tab_unit WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, unitid);
        ResultSet rs = ps.executeQuery();
        UnitInfo result = new UnitInfo();
        if(rs.next()){
            result.id(rs.getInt(1))
                    .name(rs.getString(2))
                    .unitcode(rs.getString(3))
                    .address(rs.getString(4))
                    .manager(rs.getString(5))
                    .mobile(rs.getString(6))
                    .latitude(rs.getDouble(7))
                    .longitude(rs.getDouble(8));
        }
        return result;
    }
}
