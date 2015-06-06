package cn.edu.fudan.bean;

/**
 * Created by Dawnwords on 2015/6/5.
 */
public class User {
    public final String name;
    public final String password;
    private final int authority;

    public User(String name, String password, int authority) {
        this.name = name;
        this.password = password;
        this.authority = authority;
    }

    public boolean isAdmin() {
        return authority == 1;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
