package cn.edu.fudan.filter;

import cn.edu.fudan.util.Session;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dawnwords on 2015/6/7.
 */
public abstract class BaseFilter implements Filter {

    private String[] whiteList;

    protected BaseFilter(String[] whiteList) {
        this.whiteList = whiteList;
    }

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        Session session = new Session((HttpServletRequest) req, (HttpServletResponse) resp);

        if (whiteList(session.requestURI()) || !filter(session)) {
            chain.doFilter(req, resp);
        }
    }

    private boolean whiteList(String requestURI) {
        for (String urlPattern : whiteList) {
            if (requestURI.contains(urlPattern)) {
                return true;
            }
        }
        return false;
    }

    protected abstract boolean filter(Session session);
}
