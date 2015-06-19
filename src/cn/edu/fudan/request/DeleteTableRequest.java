package cn.edu.fudan.request;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/6/18.
 */
public class DeleteTableRequest {
    private int id;
    private String table;

    public DeleteTableRequest(int id, String table) {
        this.id = id;
        this.table = table;
    }

    public int id() {
        return id;
    }

    public Table table() {
        try {
            return Table.valueOf(table.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Table.DEFAULT;
        }
    }

    public enum Table {
        UNIT("tab_unit"), SALES("tab_sales"), USER("tab_user"), DEFAULT("");
        public String table;

        Table(String table) {
            this.table = table;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
