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
//package org.kuali.ole.docstore.discovery.servlet;
//
//import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//
///**
// * Class for getting configuration info about document categories, types, formats etc.
// */
//public class DiscoveryConfigServlet extends HttpServlet {
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * @see javax.servlet.http.HttpServlet#HttpServlet()
//     */
//    public DiscoveryConfigServlet() {
//        super();
//    }
//
//    /**
//     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
//     *      response)
//     */
//    protected void doPost(HttpServletRequest request,
//                          HttpServletResponse response) throws ServletException, IOException {
//        ServletOutputStream outputStream = null;
//        BufferedInputStream inputStream = null;
//        try {
//            outputStream = response.getOutputStream();
//            String solrHome = System.getProperty("solr.solr.home");
//            File docSearchConfigFile = new File(solrHome + "/DocSearchConfig.xml");
//            response.setContentType("text/xml");
//            response.setContentLength((int) docSearchConfigFile.length());
//            FileInputStream input = new FileInputStream(docSearchConfigFile);
//            inputStream = new BufferedInputStream(input);
//            int readBytes;
//            while ((readBytes = inputStream.read()) != -1)
//                outputStream.write(readBytes);
//        } catch (IOException ioe) {
//            throw new ServletException(ioe.getMessage());
//        } finally {
//            if (outputStream != null)
//                outputStream.close();
//            if (inputStream != null)
//                inputStream.close();
//        }
//    }
//
//
//    /**
//     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse
//     *      response)
//     */
//    protected void doGet(HttpServletRequest request,
//                         HttpServletResponse response) throws ServletException, IOException {
//        doPost(request, response);
//    }
//}
