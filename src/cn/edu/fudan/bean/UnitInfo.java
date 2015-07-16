package cn.edu.fudan.bean;

import java.util.ArrayList;

/**
 * Created by Dawnwords on 2015/7/16.
 */
public class UnitInfo {
    private ArrayList<UserInfoEntry> unitInfo;

    public UnitInfo() {
        this.unitInfo = new ArrayList<>();
    }

    public UnitInfo id(int id) {
        unitInfo.add(new UserInfoEntry().title("id").value(id).titleCh("网点序号"));
        return this;
    }

    public UnitInfo name(String name) {
        unitInfo.add(new UserInfoEntry().title("name").value(name).titleCh("区代码"));
        return this;
    }

    public UnitInfo unitcode(String unitcode) {
        unitInfo.add(new UserInfoEntry().title("unitcode").value(unitcode).titleCh("网点代码"));
        return this;
    }

    public UnitInfo address(String address) {
        unitInfo.add(new UserInfoEntry().title("address").value(address).titleCh("地址"));
        return this;
    }

    public UnitInfo manager(String manager) {
        unitInfo.add(new UserInfoEntry().title("manager").value(manager).titleCh("管理员"));
        return this;
    }

    public UnitInfo mobile(String mobile) {
        unitInfo.add(new UserInfoEntry().title("mobile").value(mobile).titleCh("电话"));
        return this;
    }

    public UnitInfo latitude(double latitude) {
        unitInfo.add(new UserInfoEntry().title("latitude").value(latitude).titleCh("纬度"));
        return this;
    }

    public UnitInfo longitude(double longitude) {
        unitInfo.add(new UserInfoEntry().title("longitude").value(longitude).titleCh("经度"));
        return this;
    }

    private static class UserInfoEntry {
        String title;
        String titleCh;
        Object value;

        UserInfoEntry title(String title) {
            this.title = title;
            return this;
        }

        UserInfoEntry titleCh(String titleCh) {
            this.titleCh = titleCh;
            return this;
        }

        UserInfoEntry value(Object value) {
            this.value = value;
            return this;
        }
    }
}
