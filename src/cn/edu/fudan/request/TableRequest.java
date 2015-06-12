package cn.edu.fudan.request;

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

    public static enum Table {
        UNIT(new String[]{"id", "name", "unitcode", "address", "manager", "mobile", "unitnum", "area", "population1", "population2"},
                new String[]{"Int", "String", "String", "String", "String", "String", "Int", "Double", "Int", "Int"},
                new int[]{1, 3, 4}, "tab_unit"),
        SALE(new String[]{"id", "unitid", "saleyear", "salequarter", "salemonth", "s1", "s2", "s3", "stotal"},
                new String[]{"Int", "Int", "String", "String", "String", "Double", "Double", "Double", "Double"},
                new int[]{1, 2, 3, 4}, "tab_sales"),
        USER(new String[]{"id", "name", "authority"},
                new String[]{"Int", "String", "String"},
                new int[]{1}, "tab_user"),
        DEFAULT(new String[0], new String[0], new int[0], "");
        public final String[] cols;
        public final String[] colTypes;
        public final int[] searchCols;
        public final String table;

        Table(String[] cols, String[] colTypes, int[] searchCols, String table) {
            this.cols = cols;
            this.colTypes = colTypes;
            this.searchCols = searchCols;
            this.table = table;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
