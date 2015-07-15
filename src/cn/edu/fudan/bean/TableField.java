package cn.edu.fudan.bean;

/**
 * Created by Dawnwords on 2015/6/16.
 */
public class TableField {
    private String title;
    private String ftype;
    private String titleCh;

    private TableField(String title, String ftype) {
        this.title = title;
        this.titleCh = title;
        this.ftype = ftype;
    }

    public static TableField input(String title) {
        return new TableField(title, "input");
    }

    public static TableField disabled(String title) {
        return new TableField(title, "disabled");
    }

    public TableField ch(String titleCh) {
        this.titleCh = titleCh;
        return this;
    }
}
