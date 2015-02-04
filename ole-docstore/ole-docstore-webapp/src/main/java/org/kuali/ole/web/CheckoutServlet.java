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
//import org.kuali.ole.repository.DocumentStoreManager;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
//* User: Peri Subrahmanya
//* Date: 4/7/11
//* Time: 11:30 AM
//* To change this template use File | Settings | File Templates.
//*/
//public class CheckoutServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String uuid = req.getParameter("uuid");
//        callService(resp, uuid);
//    }
//
//    private void callService(HttpServletResponse resp, String uuid) throws IOException {
//        try {
//            String checkedOutContent = new DocumentStoreManager().checkOut(uuid, "webappUser", "CheckoutServlet.callService()");
//            resp.setCharacterEncoding("UTF-8");
//            PrintWriter out = resp.getWriter();
//            out.println(checkedOutContent);
//            out.flush();
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            PrintWriter out = resp.getWriter();
//            out.println("Problem checking the file! Please refer application logs for details");
//            out.flush();
//            out.close();
//        }
//    }
//}
