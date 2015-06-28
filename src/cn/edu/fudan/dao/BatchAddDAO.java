package cn.edu.fudan.dao;

import cn.edu.fudan.bean.BatchAddResult;
import cn.edu.fudan.request.AddTableRequest;

import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/27.
 */
public class BatchAddDAO extends BaseDAO<BatchAddResult> {
    private List<AddTableRequest> adds;

    public BatchAddDAO(HttpServlet servlet, List<AddTableRequest> adds) {
        super(servlet);
        this.adds = adds;
    }

    @Override
    protected BatchAddResult processData(Connection connection) throws Exception {
        BatchAddResult result = new BatchAddResult();
        for (AddTableRequest add : adds) {
            int id = -1;
            try {
                id = new AddTableDAO(null, add).processData(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (id < 0) {
                result.addFail(add.addValue());
            } else {
                result.addSuccess();
            }
        }
        return result;
    }
}
