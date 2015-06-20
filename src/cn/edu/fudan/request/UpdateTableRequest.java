package cn.edu.fudan.request;

import cn.edu.fudan.util.TypeUtil;
import cn.edu.fudan.util.TypeUtil.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/12.
 */
public class UpdateTableRequest {
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
        List<String> legalFields = Arrays.asList(table().cols);
        for (Update update : updates) {
            int index = legalFields.indexOf(update.field);
            if (index >= 0) {
                update.type = table().colTypes[index];
                Parser parser = TypeUtil.string2Parser(update.type);
                if (parser != null) {
                    update.origin = parser.parse(update.origin);
                    update.update = parser.parse(update.update);
                    if (update.shouldUpdate()) {
                        result.add(update);
                    }
                }
            }
        }
        return result;
    }

    public static class Update {
        private String field;
        private Object origin, update;
        private String type;

        public String type() {
            return type;
        }

        public String field() {
            return field;
        }

        public Object origin() {
            return origin;
        }

        public Object update() {
            return update;
        }

        private boolean shouldUpdate() {
            return !(update == null || update.equals(origin));
        }
    }

    public enum Table {
        UNIT(new String[]{"name", "unitcode", "address", "manager", "mobile", "area", "population1", "population2"},
                new String[]{"String", "String", "String", "String", "String", "Double", "Int", "Int"},
                "tab_unit"),
        SALES(new String[]{"s1", "s2", "s3", "stotal"},
                new String[]{"Double", "Double", "Double", "Double"},
                "tab_sales"),
        USER(new String[]{"authority"},
                new String[]{"String"},
                "tab_user"),
        DEFAULT(new String[0], new String[0], "");
        private final String[] cols;
        private final String[] colTypes;
        public final String table;

        Table(String[] cols, String[] colTypes, String table) {
            this.cols = cols;
            this.colTypes = colTypes;
            this.table = table;
        }
    }

}
