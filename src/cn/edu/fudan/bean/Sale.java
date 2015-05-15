package cn.edu.fudan.bean;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/5/16.
 */
public class Sale {
    private int unitid;
    private String saleyear;
    private String salemonth;
    private double s1, s2, s3, stole;

    public Sale() {
    }

    public Sale(int unitid, String saleyear, String salemonth, double s1, double s2, double s3, double stole) {
        this.unitid = unitid;
        this.saleyear = saleyear;
        this.salemonth = salemonth;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.stole = stole;
    }

    public int getUnitid() {
        return unitid;
    }

    public void setUnitid(int unitid) {
        this.unitid = unitid;
    }

    public String getSaleyear() {
        return saleyear;
    }

    public void setSaleyear(String saleyear) {
        this.saleyear = saleyear;
    }

    public String getSalemonth() {
        return salemonth;
    }

    public void setSalemonth(String salemonth) {
        this.salemonth = salemonth;
    }

    public double getS1() {
        return s1;
    }

    public void setS1(double s1) {
        this.s1 = s1;
    }

    public double getS2() {
        return s2;
    }

    public void setS2(double s2) {
        this.s2 = s2;
    }

    public double getS3() {
        return s3;
    }

    public void setS3(double s3) {
        this.s3 = s3;
    }

    public double getStole() {
        return stole;
    }

    public void setStole(double stole) {
        this.stole = stole;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
