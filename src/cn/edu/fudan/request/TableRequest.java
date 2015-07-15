package cn.edu.fudan.request;

import cn.edu.fudan.bean.Field;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dawnwords on 2015/6/10.
 */
public class TableRequest {
    private int draw;
    private String table;
    private List<Order> order;
    private int start;
    private int length;
    private Search search;

    public int draw() {
        return draw;
    }

    public Table table() {
        try {
            return Table.valueOf(table.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Table.DEFAULT;
        }
    }

    public List<Order> order() {
        return order;
    }

    public int start() {
        return start;
    }

    public int length() {
        return length;
    }

    public String search() {
        return search == null || search.value == null || "".equals(search.value) ? null : search.value;
    }

    private static class Search {
        public String value;
    }

    public static class Order {
        private int column;
        private String dir;

        public int column() {
            return column;
        }

        public String dir() {
            return dir;
        }
    }

    public enum Table {
        UNIT(new Field[]{
                Field.Int("id"),
                Field.Int("fatherid"),
                Field.String("name").search(),
                Field.String("unitcode"),
                Field.String("address").search(),
                Field.String("manager").search(),
                Field.String("mobile"),
                Field.Int("unitnum"),
                Field.Double("area"),
                Field.Int("population1"),
                Field.Int("population2")
        }, "tab_unit"),
        SALES(new Field[]{
                Field.Int("id"),
                Field.Int("unitid").search(),
                Field.String("saleyear").search(),
                Field.String("salequarter").search(),
                Field.String("salemonth").search(),
                Field.Double("s1"),
                Field.Double("s2"),
                Field.Double("s3"),
                Field.Double("stotal"),
        }, "tab_sales"),
        USER(new Field[]{
                Field.Int("id"),
                Field.String("name").search(),
                Field.String("authority")
        }, "tab_user"),
        DEFAULT(new Field[0], "");

        public final String table;
        public final Field[] fields;

        Table(Field[] fields, String table) {
            this.fields = fields;
            this.table = table;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
