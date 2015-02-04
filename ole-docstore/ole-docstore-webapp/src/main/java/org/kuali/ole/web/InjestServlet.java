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
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
///**
//* Created by IntelliJ IDEA.
//* User: Peri Subrahmanya
//* Date: 3/31/11
//* Time: 8:11 AM
//* To change this template use File | Settings | File Templates.
//*/
//public class InjestServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	private static final Logger LOG              = LoggerFactory.getLogger(InjestServlet.class);
//    Map<String, String> fieldsMap;
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
////        List<FileItem> items = null;
////        try {
////            items = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(req);
////        } catch (FileUploadException e) {
////            throw new ServletException("Cannot parse multipart request.", e);
////        }
////        // Need to loop over all the "regular" fields first, so that we have
////        // their values for the code below
////        for (FileItem item : items) {
////            if (item.isFormField()) {
////                getFieldsMap().put(item.getFieldName(), item.getString().toLowerCase());
////            }
////        }
////        for (FileItem item : items) {
////        	if (!item.isFormField()) {
////                String filename = FilenameUtils.getName(item.getName());
////                //File file = File.createTempFile(filename, "");
////				File tempDir = new File(getDocumentStoreContentManager()
////						.getDocumentManager().getIngestFolder(
////								getFieldsMap().get("category"),
////								getFieldsMap().get("format")));
////                File file = File.createTempFile(filename, "", tempDir);
////                try {
////                    item.write(file);
////                    Map<String, String> uuidMap = getDocumentStoreContentManager()
////                    		.persistBulk(getFieldsMap().get("category"),
////                    					 getFieldsMap().get("format"), file, "webappUser", "InjestServlet.doPost()");
////                    req.setAttribute("mapResults", uuidMap);
////                    req.setAttribute("fileName", filename);
////                } catch (Exception e) {
////                	LOG.error("Problem ingesting the file! Please refer application logs for details",e);
////                    PrintWriter out = resp.getWriter();
////                    out.println("Problem ingesting the file! Please refer application logs for details");
////                    out.flush();
////                    out.close();
////                } finally {
////                	file.delete();
////                }
////            }
////        }
////        RequestDispatcher rd = getServletContext().getRequestDispatcher("/injestResults.jsp");
////        rd.forward(req, resp);
//    }
//
//    public Map<String, String> getFieldsMap() {
//        if (null == fieldsMap) {
//            fieldsMap = new HashMap<String, String>();
//        }
//        return fieldsMap;
//    }
//}
