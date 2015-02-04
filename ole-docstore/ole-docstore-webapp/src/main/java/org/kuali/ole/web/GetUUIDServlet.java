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

import org.kuali.ole.RepositoryBrowser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: peris
 * Date: 5/23/11
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetUUIDServlet extends HttpServlet {
    private static final String RESULTS_JSP =
            "/getUUIDResults.jsp";
    private RepositoryBrowser repositoryBrowser;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> uuidsList = new ArrayList<String>();
        String category = req.getParameter("category");
        String type = req.getParameter("type");
        String format = req.getParameter("format");
        String numUUIDs = req.getParameter("numUUIDs");
        try {
            if (null != category && null != format && null != numUUIDs) {
                uuidsList = getRepositoryBrowser().getUUIDs(category.toLowerCase(), type.toLowerCase(), format.toLowerCase(), new Integer(numUUIDs));
            }
        } catch (Exception e) {
            uuidsList.add(e.getMessage());
        }
        RequestDispatcher rd = getServletContext().getRequestDispatcher(RESULTS_JSP);
        req.setAttribute("result", uuidsList);
        rd.forward(req, resp);
    }


    public RepositoryBrowser getRepositoryBrowser() {
        if (null == repositoryBrowser) {
            repositoryBrowser = new RepositoryBrowser();
        }
        return repositoryBrowser;
    }
}
