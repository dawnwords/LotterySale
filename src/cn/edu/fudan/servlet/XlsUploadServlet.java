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
import java.util.ArrayList;
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
        response.setContentType("application/json");
        Result result;
        if (request.getContentType().contains("multipart/form-data")) {
            try {
                FileItem fileItem = prepareUploadFile().parseRequest(request).get(0);
                Sheet sheet = WorkbookFactory.create(fileItem.getInputStream()).getSheetAt(0);
                String table = sheet.getSheetName();
                Iterator<Row> it = sheet.iterator();
                List<String> fieldList = getFieldList(it);
                SuccessResult successResult = new SuccessResult();
                while (it.hasNext()) {
                    Row row = it.next();
                    AddTableRequest addRequest = new AddTableRequest(table);
                    int i = 0;
                    for (String field : fieldList) {
                        addRequest.addAdd(field, row.getCell(i++).getStringCellValue());
                    }
                    addResult(successResult, addRequest);
                }
                result = successResult;
            } catch (FileUploadException e) {
                result = new ErrorResult("文件上传失败");
            } catch (InvalidFormatException e) {
                result = new ErrorResult("xls文件格式不正确");
            } catch (Exception e) {
                result = new ErrorResult("上传文件不是xls文件");
            }
        } else {
            result = new ErrorResult("没有文件上传");
        }
        PrintWriter writer = response.getWriter();
        writer.write(BaseServlet.gson.toJson(result));
        writer.flush();
    }

    private void addResult(SuccessResult result, AddTableRequest addRequest) {
        int id = -1;
        try {
            id = new AddTableDAO(this, addRequest).getResult();
        } catch (Exception ignore) {
        }
        if (id < 0) {
            result.addFail(addRequest.addValue());
        } else {
            result.success++;
        }
    }

    private interface Result {
    }

    private class ErrorResult implements Result {
        private String error;

        public ErrorResult(String error) {
            this.error = error;
        }
    }

    private class SuccessResult implements Result {
        private int success;
        private List<String> fails;

        private SuccessResult() {
            this.fails = new ArrayList<>();
        }

        private void addFail(String fail) {
            fails.add(fail);
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
}
