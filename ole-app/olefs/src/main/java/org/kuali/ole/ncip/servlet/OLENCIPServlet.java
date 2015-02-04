package org.kuali.ole.ncip.servlet;

import org.extensiblecatalog.ncip.v2.responder.implprof1.NCIPServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: suryar
 * Date: 7/8/13
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLENCIPServlet extends NCIPServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        super.doPost(request,response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        super.doPost(request,response);
    }

}
