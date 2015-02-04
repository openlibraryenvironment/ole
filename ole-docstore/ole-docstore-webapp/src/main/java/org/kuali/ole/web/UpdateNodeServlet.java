/*
 * Copyright 2011 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl2.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.web;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 6/12/11
 * Time: 1:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateNodeServlet extends HttpServlet {
    private static final String RESULTS_JSP =
            "/updateNodeResults.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String oldNodeName = req.getParameter("oleNodeName");
//        String newNodeName = req.getParameter("newNodeName");
//        String result;
//        try {
//            result = getDocumentStoreContentManager().updateNodeName(oldNodeName, newNodeName);
//        } catch (OleException e) {
//            result = e.getMessage();
//        }
//        RequestDispatcher rd = getServletContext().getRequestDispatcher(RESULTS_JSP);
//        req.setAttribute("result", result);
//        rd.forward(req, resp);
    }
}
