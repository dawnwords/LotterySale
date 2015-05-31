package cn.edu.fudan.request;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/5/31.
 */
public class SearchUnitRequest {
    public final String keyword;
    public final int count;

    public SearchUnitRequest(int count, String keyword) {
        this.count = count;
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
