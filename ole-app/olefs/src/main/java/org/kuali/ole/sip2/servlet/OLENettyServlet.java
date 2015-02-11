package org.kuali.ole.sip2.servlet;


import org.apache.log4j.Logger;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.service.OLESIP2HelperService;
import org.kuali.ole.sip2.service.impl.OLESIP2HelperServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 12/26/14.
 */
public class OLENettyServlet extends HttpServlet {

    OLESIP2HelperService OLESIP2HelperService = new OLESIP2HelperServiceImpl();


    Logger LOG = Logger.getLogger(OLENettyServlet.class);

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(OLESIP2Constants.ALREADY_REQUEST_PROCESSING.equalsIgnoreCase("Y")){
            StringBuffer responseString = new StringBuffer();
            responseString.append(OLESIP2Constants.REFRESH_PAGE);
            PrintWriter out = response.getWriter();
            out.print(responseString.toString());
        }else if (OLESIP2Constants.olesip2Server != null) {
            if (!OLESIP2Constants.olesip2Server.isStopped()) {
                StringBuffer responseString = new StringBuffer();
                responseString.append(OLESIP2Constants.SERVER_RUNNING);
                PrintWriter out = response.getWriter();
                out.print(responseString.toString());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer responseString = new StringBuffer();
        PrintWriter out = response.getWriter();
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();

        parameterMap = request.getParameterMap();

        String action = parameterMap.get("action")[0];
        if(OLESIP2Constants.ALREADY_REQUEST_PROCESSING.equalsIgnoreCase("N") && action.equalsIgnoreCase("start") && OLESIP2Constants.olesip2Server == null){
                    OLESIP2Constants.ALREADY_REQUEST_PROCESSING = "Y";
                    OLESIP2Constants.olesip2Server = OLESIP2HelperService.doActionForSocketServer(action, responseString, OLESIP2Constants.olesip2Server);
                    OLESIP2Constants.ALREADY_REQUEST_PROCESSING = "N";
                    out.write(responseString.toString());

        }else if(action.equalsIgnoreCase("start") && OLESIP2Constants.ALREADY_REQUEST_PROCESSING.equalsIgnoreCase("Y")){
            responseString.append(OLESIP2Constants.REFRESH_PAGE);
            out.print(responseString.toString());
        }else if(action.equalsIgnoreCase("start")){
            if (OLESIP2Constants.olesip2Server != null) {
                if (!OLESIP2Constants.olesip2Server.isStopped()) {
                    responseString.append(OLESIP2Constants.SERVER_RUNNING);
                    out.print(responseString.toString());
                }else{
                    OLESIP2Constants.ALREADY_REQUEST_PROCESSING = "Y";
                    OLESIP2Constants.olesip2Server = OLESIP2HelperService.doActionForSocketServer(action, responseString, OLESIP2Constants.olesip2Server);
                    OLESIP2Constants.ALREADY_REQUEST_PROCESSING = "N";
                    out.write(responseString.toString());
                }
            }
        }else{
            OLESIP2Constants.olesip2Server = OLESIP2HelperService.doActionForSocketServer(action, responseString, OLESIP2Constants.olesip2Server);
            out.write(responseString.toString());
        }


    }

}
