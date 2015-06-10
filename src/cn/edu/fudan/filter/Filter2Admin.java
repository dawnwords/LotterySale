package cn.edu.fudan.filter;

import cn.edu.fudan.bean.User;
import cn.edu.fudan.util.Session;

import javax.servlet.annotation.WebFilter;

/**
 * Created by Dawnwords on 2015/6/9.
 */
@WebFilter(filterName = "Filter2Admin", urlPatterns = {"/admin.jsp", "/js/admin.js", "/css/admin.css", "/admin/*"})
public class Filter2Admin extends BaseFilter {
    public Filter2Admin() {
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
