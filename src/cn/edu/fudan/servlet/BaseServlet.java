package cn.edu.fudan.servlet;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Dawnwords on 2015/5/15.
 */
public abstract class BaseServlet<T> extends HttpServlet {

    private static final Gson gson = new Gson();

    protected static final int POST = 1;
    protected static final int GET = 2;

    private int supportMethod;

    protected BaseServlet(int supportMethod) {
        this.supportMethod = supportMethod;
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if ((supportMethod & POST) != 0) {
            execute(request, response);
        }
    }

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ((supportMethod & GET) != 0) {
            execute(req, resp);
        }
    }

    private void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        try {
            writer.write(gson.toJson(processRequest(request)));
        } catch (Exception e) {
            writer.write(gson.toJson(e));
        }
        writer.flush();
    }

    protected abstract T processRequest(HttpServletRequest request) throws Exception;
}
