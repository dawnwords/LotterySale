package cn.edu.fudan.bean;

/**
 * Created by Dawnwords on 2015/6/16.
 */
public class TableField {
    private String title;
    private String ftype;

    public TableField(String title, boolean enabled) {
        this.title = title;
        this.ftype = enabled ? "input" : "disabled";
    }
}
