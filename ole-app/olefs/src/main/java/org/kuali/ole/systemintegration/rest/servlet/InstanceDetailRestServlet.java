package org.kuali.ole.systemintegration.rest.servlet;

import com.ibm.wsdl.util.StringUtils;
import org.kuali.ole.systemintegration.rest.RestConstants;
import org.kuali.ole.systemintegration.rest.service.DocstoreDataRetrieveService;
import org.kuali.ole.utility.CompressUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/10/14
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstanceDetailRestServlet extends HttpServlet{

    private static final Logger LOG = LoggerFactory
            .getLogger(InstanceDetailRestServlet.class);
    private CompressUtils compressUtils = new CompressUtils();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        float start = System.currentTimeMillis();
        String[] bibUUID = (String[])request.getParameterMap().get(RestConstants.BIB_ID);
        String acceptType = request.getHeader("Accept");
        List<String> bibIdList = new ArrayList<>();
        if(bibUUID != null){
            bibIdList = Arrays.asList(bibUUID);
        }
        boolean doProcess = true;
        String format = RestConstants.XML;
        if(acceptType == null || (acceptType!=null && acceptType.contains("*/*"))){
            format = RestConstants.XML;
        }else if(! (acceptType.contains("application/xml") || acceptType.equalsIgnoreCase("application/json") || acceptType.contains("text/xml"))){
            doProcess = false;
        }else{
            if(acceptType.contains("application/xml") || acceptType.contains("text/xml")){
                format =  RestConstants.XML;
            } else{
                format = RestConstants.JSON;
            }
        }
        if(bibUUID ==null){
            response.getWriter().println(RestConstants.BIB_ID_NULL);
        }else{
            if(doProcess){
                DocstoreDataRetrieveService oleDocstoreDataRetrieveService = new DocstoreDataRetrieveService();
                String instanceResponse = oleDocstoreDataRetrieveService.getHoldingsTree(bibIdList, format);

                if (instanceResponse == null || instanceResponse.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    if (bibIdList.size() == 1)
                        response.getWriter().println(RestConstants.NO_RECORD_RESPONSE_FOR_SINGLE_BIB + bibIdList.get(0));
                    else if (bibIdList.size() > 1)
                        response.getWriter().println(RestConstants.NO_RECORD_RESPONSE_FOR_MULTIPLE_BIBS);
                } else {
                    if (acceptType == null || (acceptType!=null && acceptType.contains("*/*"))) {
                        response.setContentType(RestConstants.CONTENT_TYPE);
                    } else if (format.equalsIgnoreCase(RestConstants.XML)) {
                        response.setContentType(acceptType);
                    } else if (format.equalsIgnoreCase(RestConstants.JSON)) {
                        response.setContentType(acceptType);
                    }
                    response.getWriter().println(instanceResponse);
                }
            }else{
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, RestConstants.INVALID_FORAMAT);
            }
        }
        float end = System.currentTimeMillis();
        LOG.info("TimeTaken :" + (end-start)/1000 );
    }

    private List<String> getBibIdList(String bibUUIDs) {
        List<String> bibIdsList = new ArrayList<String>();
        String[] queueArray = bibUUIDs.split(",");
        for (int i = 0; i < queueArray.length; i++) {
            bibIdsList.add(queueArray[i]);
        }
        return bibIdsList;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
}
