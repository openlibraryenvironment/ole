//package org.kuali.ole.web;
//
//import org.kuali.ole.RepositoryBrowser;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * Created by IntelliJ IDEA.
// * User: PJ7789
// * Date: 2/27/12
// * Time: 12:00 PM
// * To change this template use File | Settings | File Templates.
// */
//public class RepositoryDataServlet
//        extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        callService(resp);
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        callService(resp);
//    }
//
//    private void callService(HttpServletResponse resp) throws IOException {
//        try {
//            String repositoryDump = new RepositoryBrowser().getRepositoryDump();
//            resp.setCharacterEncoding("UTF-8");
//            PrintWriter out = resp.getWriter();
//            out.println(repositoryDump);
//            out.flush();
//            out.close();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            PrintWriter out = resp.getWriter();
//            out.println("Problem in getting the repository data ! Please refer application logs for details");
//            out.flush();
//            out.close();
//        }
//    }
//}
