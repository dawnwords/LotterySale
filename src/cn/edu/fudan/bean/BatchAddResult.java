package cn.edu.fudan.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/27.
 */
public class BatchAddResult {
    private int success;
    private List<String> fails;

    public BatchAddResult() {
        this.fails = new ArrayList<>();
    }

    public void addSuccess() {
        success++;
    }

    public void addFail(String fail) {
        fails.add(fail);
    }
}
