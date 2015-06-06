package cn.edu.fudan.util;

import cn.edu.fudan.bean.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dawnwords on 2015/6/5.
 */
public class SessionUtil {
    private static final String USER = "user";

    public static User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(USER);
    }

    public static void setUser(HttpServletRequest request, User user) {
        request.getSession().setAttribute(USER, user);
    }
}
