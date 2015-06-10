package cn.edu.fudan.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/10.
 */
public class TabUnit {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<List> data;

    public TabUnit() {
        this.data = new ArrayList<>();
    }

    public void draw(int draw) {
        this.draw = draw;
    }

    public void recordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public void recordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public void addData(List data) {
        this.data.add(data);
    }
}
