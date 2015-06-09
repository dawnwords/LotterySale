package cn.edu.fudan.filter;

import cn.edu.fudan.bean.User;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebFilter;

/**
 * Created by Dawnwords on 2015/6/9.
 */
@WebFilter(filterName = "AdminFilter", urlPatterns = {"/admin*"})
public class AdminFilter extends BaseFilter {
    public AdminFilter() {
        super(new String[0]);
    }

    @Override
    protected boolean filter(Session session) {
        User user = session.user();
        if (!user.isAdmin()) {
            session.redirect("/main.jsp ");
            return true;
        }
        return false;
    }
}
