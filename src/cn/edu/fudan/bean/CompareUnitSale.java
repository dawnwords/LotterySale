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
    private int population1, population2;
    private List<String> year;
    private List<String> month;
    private List<List<SaleData>> yearData;
    private List<List<SaleData>> monthData;

    public CompareUnitSale(int unitid, String unitName, int population1, int population2) {
        this.unitid = unitid;
        this.unitName = unitName;
        this.population1 = population1;
        this.population2 = population2;
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
        this.yearData.get(this.year.indexOf(year)).add(new SaleData(monthToDateString(month), s1, s2, s3, stotal));
        this.monthData.get(this.month.indexOf(month)).add(new SaleData(yearToDateString(year), s1, s2, s3, stotal));
    }

    private String monthToDateString(String month) {
        return "2014/" + month + "/01";
    }

    private String yearToDateString(String year) {
        return year + "/01/01";
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
