package org.kuali.ole.sip2.servlet;

import org.apache.log4j.Logger;
import org.kuali.ole.ncip.bo.OLENCIPConstants;
import org.kuali.ole.sip2.constants.OLESIP2Constants;
import org.kuali.ole.sip2.service.impl.OLESIP2ServiceImpl;
import org.kuali.ole.sys.OLEPropertyConstants;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OLESIP2Servlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(OLESIP2Servlet.class);
    private String service;

    OLESIP2ServiceImpl oleSip2Service = new OLESIP2ServiceImpl();


    private BusinessObjectService businessObjectService;

    protected BusinessObjectService getBusinessObjectService() {
        if (businessObjectService == null) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String responseString = "";
        Map<String, String[]> parameterMap = null;
        String outputFormat = OLESIP2Constants.SIP2_FORMAT;
        parameterMap = request.getParameterMap();
        if (parameterMap.containsKey(OLENCIPConstants.OLE_CIRCULATION_SERVICE)) {
            service = parameterMap.get(OLENCIPConstants.OLE_CIRCULATION_SERVICE)[0];
        }

        if (service != null && !service.equalsIgnoreCase("")) {
            if (service.equals(OLESIP2Constants.SIP_SERVICE)) {
                if (parameterMap.containsKey(OLESIP2Constants.REQUEST_DATA)) {

                    if (parameterMap.size() >= 2) {
                        String operatorId = OLESIP2Constants.OPERATOR_ID;
                        if (parameterMap.containsKey(OLESIP2Constants.LOGIN_USER)) {
                            Map userMap = new HashMap();
                            userMap.put(OLEPropertyConstants.PERSON_USER_IDENTIFIER, parameterMap.get(OLESIP2Constants.LOGIN_USER)[0]);
                            List<PrincipalBo> matching = (List<PrincipalBo>) getBusinessObjectService().findMatching(PrincipalBo.class, userMap);
                            if (matching != null && matching.size() >= 1) {
                                operatorId = matching.get(0).getPrincipalId();
                            }
                        }
                        responseString = oleSip2Service.processRequest(parameterMap.get(OLESIP2Constants.REQUEST_DATA)[0], service, operatorId);
                    } else {
                        responseString = oleSip2Service.getCirculationErrorMessage(service, OLENCIPConstants.PARAMETER_MISSING, "502", OLENCIPConstants.LOOKUPUSER, outputFormat);
                    }

                } else {

                    LOG.info("Unknown Service Name: " + service + "   Parameter is missing");
                    responseString = oleSip2Service.getCirculationErrorMessage(service, OLENCIPConstants.UNKNOWN_SERVICE, "503", null, outputFormat);
                    response.sendError(405, "Method Not Supported");
                }
            } else {
                responseString = OLENCIPConstants.NULL_SERVICE;
            }
            if (responseString != null) {
                responseString = responseString.replaceAll("errorMessage", OLENCIPConstants.MESSAGE);
                responseString = responseString.replaceAll("<br/>", "");
            }

            PrintWriter out = response.getWriter();

            if (responseString.contains(OLENCIPConstants.INVALID_FORMAT)) {
                response.setStatus(406);
            } else if (responseString.contains(OLENCIPConstants.PARAMETER_MISSING)) {
                response.setStatus(422);
            } else {
                response.setStatus(200);
            }
            //out.write(URLEncoder.encode(responseString, "UTF-8"));
            out.write(responseString);

        }
    }


}
