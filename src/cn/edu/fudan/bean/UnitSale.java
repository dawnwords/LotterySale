package cn.edu.fudan.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Dawnwords on 2015/5/16.
 */
public class UnitSale {
    private int unitid;
    private List<SaleData> yearData, seasonData, monthData;


    public UnitSale() {
    }

    public UnitSale(int unitid, List<SaleData> yearData, List<SaleData> seasonData, List<SaleData> monthData) {
        this.unitid = unitid;
        this.yearData = yearData;
        this.seasonData = seasonData;
        this.monthData = monthData;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
