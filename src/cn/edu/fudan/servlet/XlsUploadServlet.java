package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.AddTableDAO;
import cn.edu.fudan.request.AddTableRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dawnwords on 2015/6/22.
 */
@WebServlet(name = "XlsUploadServlet", urlPatterns = {"/admin/upload"})
public class XlsUploadServlet extends HttpServlet {
    private static final int MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final int MAX_MEM_SIZE = 5 * 1024 * 1024;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("utf-8");
        if (request.getContentType().contains("multipart/form-data")) {
            try {
                FileItem fileItem = prepareUploadFile().parseRequest(request).get(0);
                Sheet sheet = WorkbookFactory.create(fileItem.getInputStream()).getSheetAt(0);
                String table = sheet.getSheetName();
                Iterator<Row> it = sheet.iterator();
                List<String> fieldList = getFieldList(it);
                List<String> fails = new LinkedList<>();
                int success = 0;
                while (it.hasNext()) {
                    Row row = it.next();
                    AddTableRequest addRequest = new AddTableRequest(table);
                    int i = 0;
                    for (String field : fieldList) {
                        addRequest.addAdd(field, row.getCell(i++).getStringCellValue());
                    }
                    if (new AddTableDAO(this, addRequest).getResult() < 0) {
                        fails.add(addRequest.addValue());
                    } else {
                        success++;
                    }
                    sendSuccess(response, new Result(success, fails));
                }
            } catch (FileUploadException e) {
                sendError(response, "文件上传失败");
            } catch (InvalidFormatException e) {
                sendError(response, "xls文件格式不正确");
            }
        } else {
            sendError(response, "没有文件上传");
        }
    }

    private void sendSuccess(HttpServletResponse response, Result result) throws IOException {
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(BaseServlet.gson.toJson(result));
        writer.flush();
    }

    private class Result {
        private int success;
        private List<String> fails;

        public Result(int success, List<String> fails) {
            this.success = success;
            this.fails = fails;
        }
    }

    private List<String> getFieldList(Iterator<Row> iterator) {
        List<String> result = new LinkedList<>();
        Row header = iterator.next();
        for (Cell cell : header) {
            result.add(cell.getStringCellValue());
        }
        return result;
    }

    private ServletFileUpload prepareUploadFile() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MAX_MEM_SIZE);
        factory.setRepository(new File(getServletContext().getInitParameter("UploadBuffer")));
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(MAX_FILE_SIZE);
        return upload;
    }

    private void sendError(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(String.format("{error:%s}", msg));
        writer.flush();
    }
}
