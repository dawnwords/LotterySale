package cn.edu.fudan.request;

import cn.edu.fudan.bean.Field;
import cn.edu.fudan.util.TypeUtil;
import cn.edu.fudan.util.TypeUtil.Parser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/20.
 */
public class AddTableRequest {
    private String table;
    private List<Add> adds;

    public AddTableRequest(String table) {
        this.table = table;
        this.adds = new ArrayList<>();
    }

    public void addAdd(String field, Object value) {
        Add add = new Add();
        add.field = field;
        add.value = value;
        adds.add(add);
    }

    public Table table() {
        try {
            return Table.valueOf(table.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Table.DEFAULT;
        }
    }

    public String addValue() {
        String[] result = new String[adds.size()];
        int i = 0;
        for (Add add : adds) {
            result[i++] = add.value.toString();
        }
        return Arrays.toString(result);
    }

    public List<Add> adds(int level) {
        Table table = table();
        List<Add> result = new ArrayList<>();
        for (Add add : adds) {
            Field field = table.matchLevel(add.field, level);
            if (field != null) {
                add.type = field.type();
                Parser parser = TypeUtil.string2Parser(field.type());
                if (parser != null) {
                    add.value = parser.parse(add.value);
                    add.qm = "?";
                    result.add(add);
                }
            }
        }
        for (CalculateField cfield : table.calculatedFields) {
            Add add = new Add();
            add.field = cfield.field.name();
            add.type = cfield.field.type();
            add.qm = cfield.qm;
            add.value = CalculateField.LEVEL.equals(cfield.defaultVal) ? level : cfield.defaultVal;
            result.add(add);
        }
        return result;
    }

    public int id() {
        Table table = table();
        if (table.idCol < 0) return -1;
        Field field = table.fields[table.idCol];
        for (Add add : adds) {
            if (field.name().equals(add.field)) {
                return (Integer) Parser.IntParser.parse(add.value);
            }
        }
        return -1;
    }

    public static class Add {
        private String field;
        private Object value;
        private String type;
        private String qm;

        public String qm() {
            return qm;
        }

        public String field() {
            return field;
        }

        public Object value() {
            return value;
        }

        public String type() {
            return type;
        }
    }

    public enum Table {
        UNIT(new Field[]{
                Field.Int("fatherid"),
                Field.String("name"),
                Field.String("unitcode"),
                Field.String("address"),
                Field.String("manager"),
                Field.String("mobile"),
                Field.Double("area").levelLimit(1),
                Field.Int("population1").levelLimit(1),
                Field.Int("population2").levelLimit(1)},
                new CalculateField[]{
                        new CalculateField(Field.Int("level")).qm("? + 1"),
                        new CalculateField(Field.Int("unitnum")).qm("? = 2")
                }, "tab_unit", 0),
        SALES(new Field[]{
                Field.Double("s1").levelLimit(3),
                Field.Double("s2").levelLimit(3),
                Field.Double("s3").levelLimit(3),
                Field.Double("stotal").levelLimit(3),
                Field.Int("unitid"),
                Field.String("saleyear"),
                Field.String("salemonth"),
                Field.String("salequarter")
        }, new CalculateField[0], "tab_sales", 4),
        USER(new Field[]{
                Field.String("name"),
                Field.String("authority")},
                new CalculateField[]{
                        new CalculateField(Field.String("password")).qm( "password(?)").defaultVal("123456")
                }, "tab_user", -1),
        DEFAULT(new Field[0], new CalculateField[0], "", -1);

        private final Field[] fields;
        private final CalculateField[] calculatedFields;
        public final String table;
        private final int idCol;

        Table(Field[] fields, CalculateField[] calculatedFields, String table, int idCol) {
            this.fields = fields;
            this.calculatedFields = calculatedFields;
            this.table = table;
            this.idCol = idCol;
        }

        private Field matchLevel(String fieldName, int level) {
            for (Field field : fields) {
                if (field.name().equals(fieldName)) {
                    return field.matchLevel(level) ? field : null;
                }
            }
            return null;
        }
    }

    private static class CalculateField {
        private static final String LEVEL = "level";
        private Field field;
        private String qm;
        private String defaultVal;

        public CalculateField(Field field) {
            this.field = field;
            this.qm = "?";
            this.defaultVal = LEVEL;
        }

        public CalculateField qm(String qm) {
            this.qm = qm;
            return this;
        }

        public CalculateField defaultVal(String defaultVal) {
            this.defaultVal = defaultVal;
            return this;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
