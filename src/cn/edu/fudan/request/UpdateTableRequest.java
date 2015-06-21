package cn.edu.fudan.request;

import cn.edu.fudan.util.TypeUtil;
import cn.edu.fudan.util.TypeUtil.Parser;
import com.google.gson.Gson;

import java.util.ArrayList;
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
        Table table = table();
        for (Update update : updates) {
            Field field = table.getField(update.field);
            if (field != null) {
                update.type = field.type;
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
            return update != null && !update.equals(origin);
        }
    }

    public enum Table {
        UNIT(new Field[]{new Field("name", "String"), new Field("unitcode", "String"), new Field("address", "String"),
                new Field("manager", "String"), new Field("mobile", "String"), new Field("area", "Double"),
                new Field("population1", "Int"), new Field("population2", "Int")}, "tab_unit"),
        SALES(new Field[]{new Field("s1", "Double"), new Field("s2", "Double"), new Field("s3", "Double"),
                new Field("stotal", "Double")}, "tab_sales"),
        USER(new Field[]{new Field("authority", "String")}, "tab_user"),
        DEFAULT(new Field[0], "");

        private final Field[] fields;
        public final String table;

        Table(Field[] fields, String table) {
            this.fields = fields;
            this.table = table;
        }

        private Field getField(String fieldName) {
            for (Field field : fields) {
                if (field.name.equals(fieldName)) {
                    return field;
                }
            }
            return null;
        }
    }

    private static class Field {
        private String name;
        private String type;

        public Field(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
