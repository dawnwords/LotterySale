package cn.edu.fudan.request;

import cn.edu.fudan.bean.Field;

import java.util.Arrays;

/**
 * Created by Dawnwords on 2015/7/15.
 */
public class SaleWarningRequest extends TableRequest {
    private String year;
    private String month;

    private static final String[] MONTH = new String[]{"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};

    public String year() {
        return year;
    }

    public String month() {
        return month;
    }

    public String preYear() {
        if ("01".equals(month)) {
            try {
                return String.valueOf(Integer.parseInt(year) - 1);
            } catch (NumberFormatException ignored) {
            }
        }
        return year;
    }

    public String preMonth() {
        return MONTH[(Arrays.binarySearch(MONTH, month) + 11) % 12];
    }

    public static final Field[] FIELDS = new Field[]{
            Field.Int("id"),
            Field.String("name").search(),
            Field.String("address").search(),
            Field.Double("prestotal"),
            Field.Double("thisstotal"),
            Field.Double("ratio")
    };

}
