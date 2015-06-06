package cn.edu.fudan.filter;

import cn.edu.fudan.util.Session;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dawnwords on 2015/6/5.
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"/*"})
public class LoginFilter implements Filter {
    private static final String[] WHITE_LIST = {"index", "/imgs", "/login"};

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (!whiteList(request.getRequestURI()) && new Session(request).getUser() == null) {
            response.sendRedirect("/index.jsp ");
        } else {
            chain.doFilter(req, resp);
        }
    }

    private boolean whiteList(String requestURI) {
        for (String urlPattern : WHITE_LIST) {
            if (requestURI.contains(urlPattern)) {
                return true;
            }
        }
        return false;
    }

    public void init(FilterConfig config) throws ServletException {
    }

}
