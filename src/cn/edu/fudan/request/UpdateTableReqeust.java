package cn.edu.fudan.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/12.
 */
public class UpdateTableReqeust {
    private int id;
    private String table;
    private List<Update> updates;

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

    public List<Update> updates() {
        List<Update> result = new ArrayList<>();
        List<String> illegalFields = Arrays.asList(table().cols);
        for (Update update : updates) {
            if (update.shouldUpdate() && illegalFields.contains(update.field)) {
                result.add(update);
            }
        }
        return result;
    }

    public static class Update {
        public final String field;
        public final Object origin, update;

        public Update(String field, Object origin, Object update) {
            this.field = field;
            this.origin = origin;
            this.update = update;
        }

        private boolean shouldUpdate() {
            return !origin.equals(update);
        }
    }

    public enum Table {
        UNIT(new String[]{"name", "unitcode", "address", "manager", "mobile", "area", "population1", "population2"},
                new String[]{"String", "String", "String", "String", "String", "Double", "Int", "Int"},
                "tab_unit"),
        SALE(new String[]{"s1", "s2", "s3", "stotal"},
                new String[]{"Double", "Double", "Double"},
                "tab_sales"),
        USER(new String[]{"authority"},
                new String[]{"String"},
                "tab_user"),
        DEFAULT(new String[0], new String[0], "");
        public final String[] cols;
        public final String[] colTypes;
        public final String table;

        Table(String[] cols, String[] colTypes, String table) {
            this.cols = cols;
            this.colTypes = colTypes;
            this.table = table;
        }
    }
}
