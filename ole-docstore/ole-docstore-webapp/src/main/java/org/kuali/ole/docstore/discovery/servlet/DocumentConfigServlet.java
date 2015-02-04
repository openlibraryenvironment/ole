package org.kuali.ole.docstore.discovery.servlet;

/**
 * Created by IntelliJ IDEA.
 * User: SG7940
 * Date: 6/26/12
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */


import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Class for getting configuration info about document categories, types, formats etc.
 */
public class DocumentConfigServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public DocumentConfigServlet() {
        super();
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
     *      response)
     */


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        ServletOutputStream outputStream = null;
        BufferedInputStream inputStream = null;
        try {
            outputStream = response.getOutputStream();
            String documentConfigFilePath = System.getProperty("document.config.file");
            File docSearchConfigFile = new File(documentConfigFilePath);
            response.setContentType("text/xml");
            response.setContentLength((int) docSearchConfigFile.length());
            FileInputStream input = new FileInputStream(docSearchConfigFile);
            inputStream = new BufferedInputStream(input);
            int readBytes;
            while ((readBytes = inputStream.read()) != -1)
                outputStream.write(readBytes);
        } catch (IOException ioe) {
            throw new ServletException(ioe.getMessage());
        } finally {
            if (outputStream != null)
                outputStream.close();
            if (inputStream != null)
                inputStream.close();
        }
    }

}
