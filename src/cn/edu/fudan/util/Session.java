package cn.edu.fudan.util;

import cn.edu.fudan.bean.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dawnwords on 2015/6/5.
 */
public class Session {
    private static final String USER = "user";
    private HttpServletRequest request;

    public Session(HttpServletRequest request) {
        this.request = request;
    }

    public User getUser() {
        return (User) request.getSession().getAttribute(USER);
    }

    public void setUser(User user) {
        request.getSession().setAttribute(USER, user);
    }
}
