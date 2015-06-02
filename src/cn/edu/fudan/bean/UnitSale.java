package cn.edu.fudan.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dawnwords on 2015/5/16.
 */
public class UnitSale {
    private int unitid;
    private String unitName;
    private int population1, population2;
    private List<SaleData> yearData, quarterData, monthData;


    public UnitSale() {
    }

    public UnitSale(int unitid, String unitName, int population1, int population2,
                    List<SaleData> yearData, List<SaleData> quarterData, List<SaleData> monthData) {
        this.unitid = unitid;
        this.unitName = unitName;
        this.population1 = population1;
        this.population2 = population2;
        this.yearData = yearData;
        this.quarterData = quarterData;
        this.monthData = monthData;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
