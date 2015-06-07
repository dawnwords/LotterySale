package cn.edu.fudan.filter;

import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebFilter;

/**
 * Created by Dawnwords on 2015/6/5.
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"/*"})
public class LoginFilter extends BaseFilter {

    public LoginFilter() {
        super(new String[]{"index", "/imgs", "/login"});
    }

    @Override
    protected boolean filter(Session session) {
        if (session.user() == null) {
            session.redirect("/index.jsp ");
            return true;
        }
        return false;
    }

}
