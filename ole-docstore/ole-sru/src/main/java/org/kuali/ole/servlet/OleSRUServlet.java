package org.kuali.ole.servlet;

import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostic;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.bo.serachRetrieve.OleSRUSearchRetrieveResponse;
import org.kuali.ole.handler.OleSRUOpacXMLResponseHandler;
import org.kuali.ole.service.OleSRUWebService;
import org.kuali.ole.serviceimpl.OLESRUServiceImpl;
import org.kuali.ole.serviceimpl.OleSRUWebServiceImpl;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/10/12
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUServlet extends HttpServlet {

    final Logger log = LoggerFactory.getLogger(OleSRUServlet.class);

    @Override
    public void init() throws ServletException {

        log.info("init method");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet method");
       int startRecord = -1;
        OleSRUWebService oleSRUWebService = new OleSRUWebServiceImpl();
        if ((request.getParameter(OleSRUConstants.START_RECORD)) != null && !"".equals(request.getParameter(OleSRUConstants.START_RECORD))) {
            startRecord = Integer.parseInt((String) (request.getParameter(OleSRUConstants.START_RECORD)));
        } else if (request.getParameter(OleSRUConstants.START_RECORD) == null) {
            startRecord = -1;
        }
        Map reqParamMap = getReqParameters(request);
        String invalidParam = ((String) reqParamMap.get(OleSRUConstants.INVALID_PARAM));
        String xml = "";
        OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
        if((String) reqParamMap.get(OleSRUConstants.VERSION)!=null)
        oleSRUSearchRetrieveResponse.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
        else
            oleSRUSearchRetrieveResponse.setVersion(OleSRUConstants.SRU_VERSION);
        oleSRUSearchRetrieveResponse.setNumberOfRecords(new Long(0));
        if (startRecord == 0 || request.getParameter(OleSRUConstants.OperationType)==null || request.getParameter(OleSRUConstants.VERSION)==null || request.getParameter(OleSRUConstants.QUERY)==null || !((String)request.getParameter(OleSRUConstants.VERSION)).equals(OleSRUConstants.SRU_VERSION) ||((((String)request.getParameter(OleSRUConstants.RECORD_SCHEMA))!=null) && ((String)request.getParameter(OleSRUConstants.RECORD_SCHEMA)).equals(OleSRUConstants.DC_RECORD_SCHEMA))){
            OleSRUDiagnostics oleSRUDiagnostics = new OleSRUDiagnostics();
            List<OleSRUDiagnostic> oleSRUDiagnosticList = new ArrayList<OleSRUDiagnostic>();
            OleSRUDiagnostic oleSRUDiagnostic;
            oleSRUDiagnostic = new OleSRUDiagnostic();
           // oleSRUDiagnostic.setUri("info:srw/diagnostic/1/38");
            if(startRecord == 0){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/6");
            oleSRUDiagnostic.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.UN_SUPPORTED_PARAM_VALUE));
            oleSRUDiagnostic.setDetails(OleSRUConstants.START_RECORD);
            }
            if(request.getParameter(OleSRUConstants.OperationType)==null){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/7");
                oleSRUDiagnostic.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MANDATORY));
                oleSRUDiagnostic.setDetails(OleSRUConstants.OperationType);
            }
            if(request.getParameter(OleSRUConstants.VERSION)==null){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/7");
                oleSRUDiagnostic.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MANDATORY));
               if(oleSRUDiagnostic.getDetails()!=null)
                     oleSRUDiagnostic.setDetails(oleSRUDiagnostic.getDetails() + "," + OleSRUConstants.VERSION);
                    else
                oleSRUDiagnostic.setDetails(OleSRUConstants.VERSION);
            }
            if(request.getParameter(OleSRUConstants.QUERY)==null){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/7");
                oleSRUDiagnostic.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MANDATORY));
                if(oleSRUDiagnostic.getDetails()!=null)
                    oleSRUDiagnostic.setDetails(oleSRUDiagnostic.getDetails() + "," + OleSRUConstants.QUERY);
                else
                    oleSRUDiagnostic.setDetails(OleSRUConstants.QUERY);
            }
            if(request.getParameter(OleSRUConstants.RECORD_SCHEMA)!=null &&(request.getParameter(OleSRUConstants.RECORD_SCHEMA).equalsIgnoreCase(OleSRUConstants.DC_RECORD_SCHEMA))){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/7");
                oleSRUDiagnostic.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.NORECORDS_DIAGNOSTIC_MSG));
            }
            if(request.getParameter(OleSRUConstants.VERSION)!=null && !(request.getParameter(OleSRUConstants.VERSION)).equals(OleSRUConstants.SRU_VERSION)){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/5");
                oleSRUDiagnostic.setMessage(request.getParameter(OleSRUConstants.VERSION) + " " + ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.UNSUPPORT_VERSION));
                if(oleSRUDiagnostic.getDetails()!=null)  {
                    oleSRUSearchRetrieveResponse.setVersion((OleSRUConstants.SRU_VERSION));
                    oleSRUDiagnostic.setDetails(oleSRUDiagnostic.getDetails() + "," + OleSRUConstants.SRU_VERSION);
                }else{
                    oleSRUSearchRetrieveResponse.setVersion((OleSRUConstants.SRU_VERSION));
                    oleSRUDiagnostic.setDetails("Supported " + OleSRUConstants.VERSION + ":" + OleSRUConstants.SRU_VERSION);
                }
                }
            oleSRUDiagnosticList.add(oleSRUDiagnostic);
            oleSRUDiagnostics.setOleSRUDiagnosticList(oleSRUDiagnosticList);
            oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
            OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
            xml = oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,(String)reqParamMap.get("recordSchema"));
        } else if (invalidParam != null) {
            String[] invalidParams = invalidParam.split(",");
            if (invalidParams.length > 1) {
                OleSRUDiagnostics oleSRUDiagnostics = new OleSRUDiagnostics();
                List<OleSRUDiagnostic> oleSRUDiagnosticList = new ArrayList<OleSRUDiagnostic>();
                OleSRUDiagnostic oleSRUDiagnostic;
                for (int i = 1; i < invalidParams.length; i++) {
                    oleSRUDiagnostic = new OleSRUDiagnostic();
                    oleSRUDiagnostic.setUri("info:srw/diagnostic/1/8");
                    oleSRUDiagnostic.setMessage(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.UN_SUPPORTED_PARAM));
                    oleSRUDiagnostic.setDetails(invalidParams[i]);
                    oleSRUDiagnosticList.add(oleSRUDiagnostic);
                }
                OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponses = new OleSRUSearchRetrieveResponse();
                oleSRUSearchRetrieveResponses.setVersion((String) reqParamMap.get(OleSRUConstants.VERSION));
                oleSRUSearchRetrieveResponses.setNumberOfRecords(new Long(0));
                oleSRUDiagnostics.setOleSRUDiagnosticList(oleSRUDiagnosticList);
                oleSRUSearchRetrieveResponses.setOleSRUDiagnostics(oleSRUDiagnostics);
                OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
                xml = oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponses,(String)reqParamMap.get("recordSchema"));
            }
        } else {
            xml = oleSRUWebService.getOleSRUResponse(reqParamMap);
        }
        PrintWriter out = response.getWriter();
        response.setContentType("text/xml");
        response.setCharacterEncoding("UTF-8");
        out.write(xml);

    }

    public Map getReqParameters(HttpServletRequest request) {
        HashMap reqParamMap = new HashMap();
        reqParamMap.put(OleSRUConstants.OperationType, request.getParameter(OleSRUConstants.OperationType));
        reqParamMap.put(OleSRUConstants.VERSION, request.getParameter(OleSRUConstants.VERSION));
        if (request.getParameter(OleSRUConstants.QUERY) != null) {
            String query = request.getParameter(OleSRUConstants.QUERY);
            if (query.startsWith("cql.serverChoice")) {
                String[] querySplit = query.split("=");
                query = querySplit[1];
            }
            reqParamMap.put(OleSRUConstants.QUERY, query);
        }
        if ((request.getParameter(OleSRUConstants.START_RECORD)) != null && !"".equals(request.getParameter(OleSRUConstants.START_RECORD))) {
            int startRecord = Integer.parseInt((String) (request.getParameter(OleSRUConstants.START_RECORD)));
            reqParamMap.put(OleSRUConstants.START_RECORD, startRecord - 1);
        } else
            reqParamMap.put(OleSRUConstants.START_RECORD, Integer.parseInt(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.STARTRECORD)));

        if ((request.getParameter(OleSRUConstants.MAXIMUM_RECORDS)) != null && !"".equals(request.getParameter(OleSRUConstants.MAXIMUM_RECORDS))) {
            int maxRecord = Integer.parseInt((String) (request.getParameter(OleSRUConstants.MAXIMUM_RECORDS)));
            reqParamMap.put(OleSRUConstants.MAXIMUM_RECORDS, maxRecord);

        } else
            reqParamMap.put(OleSRUConstants.MAXIMUM_RECORDS, Integer.parseInt(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MAXRECORD)));

        if ((request.getParameter(OleSRUConstants.RECORD_PACKING)) != null && !"".equals(request.getParameter(OleSRUConstants.RECORD_PACKING))) {
            String recordPacking = (String) (request.getParameter(OleSRUConstants.RECORD_PACKING));
            reqParamMap.put(OleSRUConstants.RECORD_PACKING, recordPacking);
        } else
            reqParamMap.put(OleSRUConstants.RECORD_PACKING, OleSRUConstants.RECORD_PACK_XML);

        if ((request.getParameter(OleSRUConstants.SORTKEYS)) != null && !"".equals(request.getParameter(OleSRUConstants.SORTKEYS))) {
            String sortKey = (String) (request.getParameter(OleSRUConstants.SORTKEYS));
            reqParamMap.put(OleSRUConstants.SORTKEYS, sortKey);
        } else
            reqParamMap.put(OleSRUConstants.SORTKEYS, OleSRUConstants.TITLE_SORT_KEYS);

        if ((request.getParameter(OleSRUConstants.STYLE_SHEET)) != null) {
            reqParamMap.put(OleSRUConstants.STYLE_SHEET, request.getParameter(OleSRUConstants.STYLE_SHEET));
        }

        if ((request.getParameter(OleSRUConstants.RECORD_SCHEMA)) != null) {
            if (OleSRUConstants.DC_RECORD_SCHEMA.equals((String) (request.getParameter(OleSRUConstants.RECORD_SCHEMA))))
                reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.DC_RECORD_SCHEMA);
            else if (OleSRUConstants.MARC_RECORD_SCHEMA.equals((String) (request.getParameter(OleSRUConstants.RECORD_SCHEMA)))) {
                reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.MARC_RECORD_SCHEMA);
            } else
                reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, request.getParameter(OleSRUConstants.RECORD_SCHEMA));
        } else {
            reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.MARC_RECORD_SCHEMA);


        }

        setExtraRequestData(request, reqParamMap);
        return reqParamMap;

    }

    public void setExtraRequestData(HttpServletRequest request, Map reqParamMap) {

        List<String> reqParams = new ArrayList<String>(request.getParameterMap().keySet());
        reqParams.remove(OleSRUConstants.OperationType);
        reqParams.remove(OleSRUConstants.VERSION);
        reqParams.remove(OleSRUConstants.QUERY);
        reqParams.remove(OleSRUConstants.START_RECORD);
        reqParams.remove(OleSRUConstants.MAXIMUM_RECORDS);
        reqParams.remove(OleSRUConstants.RECORD_PACKING);
        reqParams.remove(OleSRUConstants.SORTKEYS);
        reqParams.remove(OleSRUConstants.RECORD_SCHEMA);
        reqParams.remove(OleSRUConstants.STYLE_SHEET);
        reqParams.remove(OleSRUConstants.RECORD_XPATH);
        reqParams.remove(OleSRUConstants.RESULTSET_TTL);
        if (reqParams.size() > 0) {
            StringBuffer invalidParameterBuffer = new StringBuffer();
            for (int i = 0; i < reqParams.size(); i++) {
                if (reqParams.get(i).contains("x-") || reqParams.get(i).contains("X-")) {
                    String extraDataKeyValue[] = reqParams.get(i).split("-");
                    String extraReqDataKey = extraDataKeyValue[extraDataKeyValue.length - 1];
                    String extraReqDataValue = (String) request.getParameter(reqParams.get(i));
                    reqParamMap.put(OleSRUConstants.EXTRA_REQ_DATA_KEY, extraReqDataKey);
                    reqParamMap.put(OleSRUConstants.EXTRA_REQ_DATA_VALUE, extraReqDataValue);
                }
                invalidParameterBuffer.append("," + reqParams.get(i));
            }
            reqParamMap.put(OleSRUConstants.INVALID_PARAM, invalidParameterBuffer.toString());
        }

    }

}