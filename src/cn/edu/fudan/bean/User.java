package cn.edu.fudan.bean;

/**
 * Created by Dawnwords on 2015/6/5.
 */
public class User {
    public final String name;
    private final Authority authority;

    public User(String name, String authority) {
        this.name = name;
        this.authority = Authority.valueOf(authority);
    }

    public boolean isAdmin() {
        return authority == Authority.Admin;
    }


    private enum Authority {
        Admin, Normal
    }
}
