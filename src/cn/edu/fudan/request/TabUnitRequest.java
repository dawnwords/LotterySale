package cn.edu.fudan.request;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/10.
 */
public class TabUnitRequest {
    private int draw;
    private List<Order> order;
    private int start;
    private int length;
    private Search search;

    public int draw() {
        return draw;
    }

    public List<Order> order() {
        return order;
    }

    public int start() {
        return start;
    }

    public int length() {
        return length;
    }

    public String search() {
        return search == null || search.value == null || "".equals(search.value) ? null : search.value;
    }

    private static class Search {
        public String value;
    }

    public static class Order {
        private int column;
        private String dir;

        public int column() {
            return column;
        }

        public String dir() {
            return dir;
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
