package cn.edu.fudan.bean;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/5/16.
 */
public class SaleData {
    private String time;
    private double s1, s2, s3, stotal;

    public SaleData(String time, double s1, double s2, double s3, double stotal) {
        this.time = time;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.stotal = stotal;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
