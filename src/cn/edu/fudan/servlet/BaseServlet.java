package cn.edu.fudan.servlet;

import cn.edu.fudan.util.Session;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by Dawnwords on 2015/5/15.
 */
public abstract class BaseServlet<RequestType, ResponseType> extends HttpServlet {

    protected static final Gson gson = new Gson();

    protected static final int POST = 1;
    protected static final int GET = 2;

    private int supportMethod;
    private Class<RequestType> requestType;

    public BaseServlet(int supportMethod, Class<RequestType> requestType) {
        this.supportMethod = supportMethod;
        this.requestType = requestType;
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response, POST);
    }

    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        execute(request, response, GET);
    }

    private void execute(HttpServletRequest request, HttpServletResponse response, int method) throws IOException {
        if ((supportMethod & method) != 0) {
            String result;
            try {
                RequestType req = gson.fromJson(new InputStreamReader(request.getInputStream(), "utf-8"), requestType);
                log("receive request:" + req);
                result = gson.toJson(processRequest(req, new Session(request)));
            } catch (Exception e) {
                result = String.format("{error: '%s:%s'}", e.getClass().getSimpleName(), e.getMessage());
            }
            log("send response:" + result);

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(result);
            writer.flush();
        }
    }

    protected abstract ResponseType processRequest(RequestType request, Session session) throws Exception;
}
