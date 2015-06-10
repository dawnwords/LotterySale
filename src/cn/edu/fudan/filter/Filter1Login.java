package cn.edu.fudan.filter;

import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebFilter;

/**
 * Created by Dawnwords on 2015/6/5.
 */
@WebFilter(filterName = "Filter1Login", urlPatterns = {"/*"})
public class Filter1Login extends BaseFilter {

    public Filter1Login() {
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
