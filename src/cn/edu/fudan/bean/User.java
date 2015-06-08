package cn.edu.fudan.bean;

/**
 * Created by Dawnwords on 2015/6/5.
 */
public class User {
    public final int id;
    public final String name;
    private final Authority authority;

    public User(int id, String authority, String name) {
        this.id = id;
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
