package cn.edu.fudan.request;

import com.google.gson.Gson;

/**
 * Created by Dawnwords on 2015/6/6.
 */
public class LoginRequest {
    public final String name;
    public final String password;

    public LoginRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
