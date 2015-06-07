package cn.edu.fudan.util;

import cn.edu.fudan.bean.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dawnwords on 2015/6/5.
 */
public class Session {
    private static final String USER = "user";
    private HttpServletRequest request;
    private HttpServletResponse response;

    public Session(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public User user() {
        return (User) request.getSession().getAttribute(USER);
    }

    public void user(User user) {
        request.getSession().setAttribute(USER, user);
    }

    public void redirect(String url) {
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String requestURI() {
        return request.getRequestURI();
    }
}
