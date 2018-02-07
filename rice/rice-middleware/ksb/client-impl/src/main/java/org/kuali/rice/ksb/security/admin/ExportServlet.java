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
package org.kuali.rice.ksb.security.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet which returns a client {@link java.security.KeyStore} object to the user as a file.  It takes in
 * the KeyStore file as a session attribute byte array.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ExportServlet extends HttpServlet {

    private static final long serialVersionUID = 3234778044685975458L;
    
    private static final String MIME_TYPE = "application/octet-stream";
//    application/pkix-cert
//    application/pkix-crl
    public static final String CLIENT_KEYSTORE_DATA = "ClientKeyStoreData";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] clientKeyStoreData = (byte[])request.getSession().getAttribute(CLIENT_KEYSTORE_DATA);
        request.getSession().removeAttribute(CLIENT_KEYSTORE_DATA);
        if (clientKeyStoreData == null) {
            throw new ServletException("No keystore file was specified.");
        }
        response.setContentType(MIME_TYPE);
        response.setContentLength(clientKeyStoreData.length);
        response.setHeader("Content-disposition", "attachment; filename="+extractFileName(request));
        response.getOutputStream().write(clientKeyStoreData);
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

    public static final String generateExportPath(String keystoreFileName, HttpServletRequest request) {
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        return basePath + "/exportsecurity/"+keystoreFileName;
    }

}
