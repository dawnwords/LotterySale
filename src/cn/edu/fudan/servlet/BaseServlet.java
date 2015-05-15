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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    protected abstract T processRequest(HttpServletRequest request) throws Exception;
}
