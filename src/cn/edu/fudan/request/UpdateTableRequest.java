package cn.edu.fudan.request;

import cn.edu.fudan.bean.Field;
import cn.edu.fudan.dao.UnitFieldDAO.LevelGetter;
import cn.edu.fudan.util.TypeUtil;
import cn.edu.fudan.util.TypeUtil.Parser;
import com.google.gson.Gson;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static cn.edu.fudan.dao.UnitFieldDAO.LevelGetter.*;

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

    public List<Update> updates(Connection connection) {
        List<Update> result = new ArrayList<>();
        Table table = table();
        int level = table.levelGetter.level(connection, id);
        for (Update update : updates) {
            Field field = table.getField(update.field, level);
            if (field != null) {
                update.type = field.type();
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
        UNIT(new Field[]{
                Field.String("name"),
                Field.String("unitcode"),
                Field.String("address"),
                Field.String("manager"),
                Field.String("mobile"),
                Field.Double("area").levelLimit(2),
                Field.Int("population1").levelLimit(2),
                Field.Int("population2").levelLimit(2)
        }, "tab_unit", UnitLevelGetter),
        SALES(new Field[]{
                Field.Double("s1").levelLimit(3),
                Field.Double("s2").levelLimit(3),
                Field.Double("s3").levelLimit(3),
                Field.Double("stotal").levelLimit(3)
        }, "tab_sales", SaleLevelGetter),
        USER(new Field[]{Field.String("authority")}, "tab_user", NullLevelGetter),
        DEFAULT(new Field[0], "", NullLevelGetter);

        private final Field[] fields;
        public final String table;
        private final LevelGetter levelGetter;

        Table(Field[] fields, String table, LevelGetter levelGetter) {
            this.fields = fields;
            this.table = table;
            this.levelGetter = levelGetter;
        }

        private Field getField(String fieldName, int level) {
            for (Field field : fields) {
                if (field.name().equals(fieldName)) {
                    return field.matchLevel(level) ? field : null;
                }
            }
            return null;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
