package cn.edu.fudan.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dawnwords on 2015/5/16.
 */
public class UnitSale {
    private int unitid;
    private String unitName;
    private List<SaleData> yearData, quarterData, monthData;


    public UnitSale() {
    }

    public UnitSale(int unitid, String unitName, List<SaleData> yearData, List<SaleData> quarterData, List<SaleData> monthData) {
        this.unitid = unitid;
        this.yearData = yearData;
        this.quarterData = quarterData;
        this.monthData = monthData;
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
