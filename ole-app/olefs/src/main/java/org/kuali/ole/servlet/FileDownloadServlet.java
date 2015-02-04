package org.kuali.ole.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by sheiksalahudeenm on 6/11/14.
 */
public class FileDownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("fileName");
        String filePath = request.getParameter("filePath");
        if(filePath!=null &&  !filePath.trim().isEmpty()){
            File file1 = new File(filePath);
            if(file1.exists()){
                if(file1.isDirectory()){
                    request.setAttribute("filePath",filePath);
                    getServletContext().getRequestDispatcher("/ListingDirectory.jsp").forward(request,response);
                }else{
                    int BUFSIZE = 4096;
                    {

                        //System.out.println("kanhiii"+filePath);
                        File file = new File(filePath);
                        int length   = 0;
                        ServletOutputStream outStream = response.getOutputStream();
                        response.setContentType("text/html");
                        response.setContentLength((int)file.length());
                        String fileName = (new File(filePath)).getName();
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

                        byte[] byteBuffer = new byte[BUFSIZE];
                        DataInputStream in = new DataInputStream(new FileInputStream(file));


                        while ((in != null) && ((length = in.read(byteBuffer)) != -1))
                        {
                            outStream.write(byteBuffer,0,length);
                        }

                        // in.close();
                        // outStream.close();
                        outStream.flush();
                    }
                }
            }
        }else{
            request.setAttribute("filePath",filePath);
            getServletContext().getRequestDispatcher("/ListingDirectory.jsp").forward(request,response);
        }
    }
}
