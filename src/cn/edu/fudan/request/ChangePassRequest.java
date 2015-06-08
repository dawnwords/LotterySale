package cn.edu.fudan.request;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/6/8.
 */
public class ChangePassRequest {
    public final String oldPass;
    public final String newPass;

    public ChangePassRequest(String oldPass, String newPass) {
        this.oldPass = oldPass;
        this.newPass = newPass;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
