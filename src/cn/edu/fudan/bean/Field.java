package cn.edu.fudan.bean;

/**
 * Created by Dawnwords on 2015/6/29.
 */
public class Field {
    private String name;
    private String type;
    private int levelLimit;

    private Field(String name, String type) {
        this.name = name;
        this.type = type;
        this.levelLimit = -1;
    }

    public static Field String(String name) {
        return new Field(name, "String");
    }

    public static Field Int(String name) {
        return new Field(name, "Int");
    }

    public static Field Double(String name) {
        return new Field(name, "Double");
    }

    public Field levelLimit(int levelLimit) {
        this.levelLimit = levelLimit;
        return this;
    }

    public String name() {
        return name;
    }

    public String type() {
        return type;
    }

    public boolean matchLevel(int level) {
        return levelLimit < 0 || levelLimit == level;
    }
}


