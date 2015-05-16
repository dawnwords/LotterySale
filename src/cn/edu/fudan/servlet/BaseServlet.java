package cn.edu.fudan.servlet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
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
            request.setCharacterEncoding("utf-8");
            String json = "", line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            while ((line = reader.readLine()) != null) {
                json += line;
            }
            log("received request:" + json);
            RequestType req = gson.fromJson(json, requestType);

            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            try {
                json = gson.toJson(processRequest(req));
            } catch (Exception e) {
                json = gson.toJson(e.getMessage());
            }
            log("send response:" + json);
            writer.write(json);
            writer.flush();
        }
    }

    protected abstract ResponseType processRequest(RequestType request) throws Exception;
}
