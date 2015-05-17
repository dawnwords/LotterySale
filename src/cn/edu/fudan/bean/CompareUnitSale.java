package cn.edu.fudan.bean;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/5/17.
 */
public class CompareUnitSale {
    private int unitid;
    private String unitName;
    private List<String> year;
    private List<String> month;
    private List<List<SaleData>> yearData;
    private List<List<SaleData>> monthData;

    public CompareUnitSale(int unitid, String unitName) {
        this.unitid = unitid;
        this.unitName = unitName;
        this.year = new ArrayList<>();
        this.month = new ArrayList<>();
        this.yearData = new ArrayList<>();
        this.monthData = new ArrayList<>();
    }

    public void pushData(String year, String month, double s1, double s2, double s3, double stotal) {
        if (!this.year.contains(year)) {
            this.year.add(year);
            this.yearData.add(new ArrayList<SaleData>());
        }
        if (!this.month.contains(month)) {
            this.month.add(month);
            this.monthData.add(new ArrayList<SaleData>());
        }
        this.yearData.get(this.year.indexOf(year)).add(new SaleData(month, s1, s2, s3, stotal));
        this.monthData.get(this.month.indexOf(month)).add(new SaleData(year, s1, s2, s3, stotal));
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}