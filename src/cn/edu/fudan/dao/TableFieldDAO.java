package cn.edu.fudan.dao;

import cn.edu.fudan.bean.TableField;
import cn.edu.fudan.request.TableFieldRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;

/**
 * Created by Dawnwords on 2015/6/16.
 */
public class TableFieldDAO extends BaseDAO<TableField[]> {
    private TableFieldRequest request;

    private static final TableField[] SALES_LV3 = {
            TableField.disabled("id").ch("序号"),
            TableField.disabled("unitid").ch("网点序号"),
            TableField.disabled("saleyear").ch("年份"),
            TableField.disabled("salequater").ch("季度"),
            TableField.disabled("salemonth").ch("月份"),
            TableField.input("s1").ch("电脑销量"),
            TableField.input("s2").ch("即开销量"),
            TableField.input("s3").ch("中福在线销量"),
            TableField.input("stotal").ch("总销量")
    };
    private static final TableField[] SALES_OTHER = {
            TableField.disabled("id").ch("序号"),
            TableField.disabled("unitid").ch("网点序号"),
            TableField.disabled("saleyear").ch("年份"),
            TableField.disabled("salequater").ch("季度"),
            TableField.disabled("salemonth").ch("月份"),
            TableField.disabled("s1").ch("电脑销量"),
            TableField.disabled("s2").ch("即开销量"),
            TableField.disabled("s3").ch("中福在线销量"),
            TableField.disabled("stotal").ch("总销量")
    };
    private static final TableField[] UNIT_LV2 = {
            TableField.disabled("id").ch("网点序号"),
            TableField.disabled("fatherid").ch("上级序号"),
            TableField.input("name").ch("区代码"),
            TableField.input("unicode").ch("网点代码"),
            TableField.input("address").ch("地址"),
            TableField.input("manager").ch("管理员"),
            TableField.input("mobile").ch("手机"),
            TableField.disabled("unitnum").ch("子网点数"),
            TableField.input("area").ch("面积"),
            TableField.input("population1").ch("户籍人口"),
            TableField.input("population2").ch("实有人口")
    };
    private static final TableField[] UNIT_OTHER = {
            TableField.disabled("id").ch("网点序号"),
            TableField.disabled("fatherid").ch("上级序号"),
            TableField.input("name").ch("区代码"),
            TableField.input("unicode").ch("网点代码"),
            TableField.input("address").ch("地址"),
            TableField.input("manager").ch("管理员"),
            TableField.input("mobile").ch("手机"),
            TableField.disabled("unitnum").ch("子网点数"),
            TableField.disabled("area").ch("面积"),
            TableField.disabled("population1").ch("户籍人口"),
            TableField.disabled("population2").ch("实有人口")
    };

    public TableFieldDAO(HttpServlet servlet, TableFieldRequest request) {
        super(servlet);
        this.request = request;
    }

    @Override
    protected TableField[] processData(Connection connection) throws Exception {
        int level = UnitFieldDAO.level(connection, request.unitid);
        if ("sales".equals(request.table)) {
            return level == 3 ? SALES_LV3 : SALES_OTHER;
        }
        if ("unit".equals(request.table)) {
            return level == 2 ? UNIT_LV2 : UNIT_OTHER;
        }
        return null;
    }
}
