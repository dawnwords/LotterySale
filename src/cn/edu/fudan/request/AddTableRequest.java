package cn.edu.fudan.request;

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
                add.type = field.type;
                Parser parser = TypeUtil.string2Parser(field.type);
                if (parser != null) {
                    add.value = parser.parse(add.value);
                    add.qm = "?";
                    result.add(add);
                }
            }
        }
        for (CalculateField cfield : table.calculatedFields) {
            Add add = new Add();
            add.field = cfield.field.name;
            add.type = cfield.field.type;
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
            if (field.name.equals(add.field)) {
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
        UNIT(new Field[]{new Field("fatherid", "Int", -1), new Field("name", "String", -1), new Field("unitcode", "String", -1),
                new Field("address", "String", -1), new Field("manager", "String", -1), new Field("mobile", "String", -1),
                new Field("area", "Double", 1), new Field("population1", "Int", 1), new Field("population2", "Int", 1)},
                new CalculateField[]{new CalculateField("level", "Int", -1, "? + 1"), new CalculateField("unitnum", "Int", -1, "? = 2")}, "tab_unit", 0),
        SALES(new Field[]{new Field("s1", "Double", 3), new Field("s2", "Double", 3), new Field("s3", "Double", 3),
                new Field("stotal", "Double", 3), new Field("unitid", "Int", 3), new Field("saleyear", "String", 3),
                new Field("salemonth", "String", 3), new Field("salequater", "String", 3)}, new CalculateField[0], "tab_sales", 0),
        USER(new Field[]{new Field("name", "String", -1), new Field("authority", "String", -1)},
                new CalculateField[]{new CalculateField("password", "String", -1, "password(?)", "123456")}, "tab_user", -1),
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
                if (field.name.equals(fieldName)) {
                    return field.levelLimit < 0 || field.levelLimit == level ? field : null;
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

        public CalculateField(String name, String type, int levelLimit, String qm) {
            this(name, type, levelLimit, qm, LEVEL);
        }

        public CalculateField(String name, String type, int levelLimit, String qm, String defaultVal) {
            this.field = new Field(name, type, levelLimit);
            this.qm = qm;
            this.defaultVal = defaultVal;
        }
    }

    private static class Field {
        private String name;
        private String type;
        private int levelLimit;

        public Field(String name, String type, int levelLimit) {
            this.name = name;
            this.type = type;
            this.levelLimit = levelLimit;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
