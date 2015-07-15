package cn.edu.fudan.servlet;

import cn.edu.fudan.dao.BatchAddDAO;
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
        PrintWriter writer = response.getWriter();
        writer.write(BaseServlet.gson.toJson(getResult(request)));
        writer.flush();
    }

    private Object getResult(HttpServletRequest request) {
        if (request.getContentType().contains("multipart/form-data")) {
            try {
                FileItem fileItem = prepareUploadFile().parseRequest(request).get(0);
                Sheet sheet = WorkbookFactory.create(fileItem.getInputStream()).getSheetAt(0);
                String table = sheet.getSheetName();
                Iterator<Row> it = sheet.iterator();
                List<String> fieldList = getFieldList(it);
                List<AddTableRequest> adds = new ArrayList<>();
                while (it.hasNext()) {
                    Row row = it.next();
                    AddTableRequest addRequest = new AddTableRequest(table);
                    if(addRequest.table()== AddTableRequest.Table.DEFAULT) {
                        return new ErrorResult("Sheet名应为unit或sales或user");
                    }
                    int i = 0;
                    for (String field : fieldList) {
                        addRequest.addCell(field, row.getCell(i++));
                    }
                    adds.add(addRequest);
                }
                return new BatchAddDAO(this, adds).getResult();
            } catch (FileUploadException e) {
                return new ErrorResult("文件上传失败");
            } catch (InvalidFormatException e) {
                return new ErrorResult("xls文件格式不正确");
            } catch (Exception e) {
                return new ErrorResult("上传文件不是xls文件");
            }
        } else {
            return new ErrorResult("没有文件上传");
        }
    }

    private class ErrorResult {
        private String error;

        public ErrorResult(String error) {
            this.error = error;
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
