package cn.edu.fudan.bean;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/5/31.
 */
public class UnitSearch {
    private int id;
    private String name;

    public UnitSearch(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
