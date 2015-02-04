///*
// * Copyright 2011 The Kuali Foundation.
// *
// * Licensed under the Educational Community License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.opensource.org/licenses/ecl2.php
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.kuali.ole.web;
//
//import org.apache.commons.fileupload.FileItem;
//import org.apache.commons.fileupload.FileUploadException;
//import org.apache.commons.fileupload.disk.DiskFileItemFactory;
//import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.io.FilenameUtils;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.*;
//
///**
//* User: Peri Subrahmanya
//* Date: 4/7/11
//* Time: 11:30 AM
//* To change this template use File | Settings | File Templates.
//*/
//public class CheckinServlet extends HttpServlet {
//    private static final String RESULTS_JSP =
//            "/checkinResults.jsp";
//    private Map<String, String> fieldsMap;
//    private HashMap<String, FileItem> fileItemMap;
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        List<FileItem> items = null;
//        try {
//            items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
//        } catch (FileUploadException e) {
//            throw new ServletException("Cannot parse multipart request.", e);
//        }
//        for (FileItem item : items) {
//            if (item.isFormField()) {
//                getFieldsMap().put(item.getFieldName(), item.getString());
//            }
//        }
//
//        for (FileItem item : items) {
//            String filename = FilenameUtils.getName(item.getName());
//            fileItemMap = new HashMap<String, FileItem>();
//            fileItemMap.put(filename, item);
//            getFieldsMap().put("fileName", filename);
//        }
//        callService(req, resp);
//    }
//
//    private void callService(HttpServletRequest req, HttpServletResponse resp) throws IOException {
////        try {
////            getDocumentStoreContentManager().checkin(null, getUUID(), "webappUser", "CheckinServlet.callService()");
////            RequestDispatcher rd = getServletContext().getRequestDispatcher(RESULTS_JSP);
////            req.setAttribute("result", "Successfully checked in");
////            rd.forward(req, resp);
////        } catch (Exception e) {
////            e.printStackTrace();
////            PrintWriter out = resp.getWriter();
////            out.println("Problem checking the file! Please refer application logs for details");
////            out.flush();
////            out.close();
////        }
//    }
//
//    public Map<String, String> getFieldsMap() {
//        if (null == fieldsMap) {
//            fieldsMap = new HashMap<String, String>();
//        }
//        return fieldsMap;
//    }
//
//    private String getUUID() {
//        Set<String> strings = getFieldsMap().keySet();
//        for (Iterator<String> iterator = strings.iterator(); iterator.hasNext();) {
//            String key = iterator.next();
//            if (key.equals("uuid")) {
//                return getFieldsMap().get(key);
//            }
//        }
//        return null;
//    }
//
//    public HashMap<String, FileItem> getFileItemMap() {
//        return fileItemMap;
//    }
//
//    public FileItem getItem() {
//        String fileName = getFieldsMap().get("fileName");
//        return getFileItemMap().get(fileName);
//    }
//}
