package cn.edu.fudan.request;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/6/16.
 */
public class TableFieldRequest {
    public final int unitid;
    public final String table;

    public TableFieldRequest(String table, int unitid) {
        this.table = table;
        this.unitid = unitid;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
