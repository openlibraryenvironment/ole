/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.core.web.impex;

import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.impex.ExportDataSet;
import org.kuali.rice.core.api.impex.xml.XmlExporterService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A servet which generates and returns a file conforming to the specified {@link ExportFormat} 
 * with the exported data in it.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExportServlet extends HttpServlet {

    private static final long serialVersionUID = -7766819916650887737L;
    
    public static final String EXPORT_DATA_SET_KEY = "ExportDataSet";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ExportDataSet dataSet = (ExportDataSet)request.getSession().getAttribute(EXPORT_DATA_SET_KEY);
        request.getSession().removeAttribute(EXPORT_DATA_SET_KEY);
        if (dataSet == null) {
            throw new ServletException("No data set was specified.");
        }
        String contentType = "application/xml";
        XmlExporterService exporter = CoreApiServiceLocator.getXmlExporterService();
        byte[] data = exporter.export(dataSet);
        response.setContentType(contentType);
        response.setContentLength(data.length);
        response.setHeader("Content-disposition", "attachment; filename="+extractFileName(request));
        response.getOutputStream().write(data);
        response.getOutputStream().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private String extractFileName(HttpServletRequest request) {
        String path = request.getPathInfo();
        int index = path.lastIndexOf('/');
        if (index >= 0) {
            path = path.substring(index+1);
        }
        return path;
    }

    public static final String generateExportPath(HttpServletRequest request, ExportDataSet dataSet) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh_mm_ss");
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return basePath + "/export/wf-export-"+format.format(new Date())+".xml";
    }

}
