package cn.edu.fudan.bean;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/5/31.
 */
public class UnitSearch {
    private int id;
    private String name;
    private int level;

    public UnitSearch(int id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public int id() {
        return id;
    }

    public int level() {
        return level;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
