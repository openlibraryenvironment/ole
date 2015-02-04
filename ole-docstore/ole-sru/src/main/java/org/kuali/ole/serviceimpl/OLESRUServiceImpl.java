package org.kuali.ole.serviceimpl;

import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostic;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.bo.serachRetrieve.OleSRUSearchRetrieveResponse;
import org.kuali.ole.handler.OleSRUOpacXMLResponseHandler;
import org.kuali.ole.service.OLESRUService;
import org.kuali.ole.service.OleSRUWebService;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 9/4/13
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESRUServiceImpl implements OLESRUService {


    @Override
    public String getDetails(String operation, String query, String version, String startRecord, String maximumRecords, String recordPacking, String recordSchema,String sortKeys,String recordXPath,String resultSetTTL,String styleSheet) {

        int startRecords = -1;

        if (startRecord != null && !"".equals(startRecord)) {
            startRecords = Integer.parseInt(startRecord);
        } else if (startRecord == null) {
            startRecords = -1;
        }
        String xml="";

        OleSRUWebService oleSRUWebService = new OleSRUWebServiceImpl();
        Map requestMap = getReqParameters(operation,query,version,startRecord,maximumRecords,recordPacking,recordSchema,sortKeys,recordXPath,resultSetTTL,styleSheet);
        OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse = new OleSRUSearchRetrieveResponse();
        oleSRUSearchRetrieveResponse.setVersion(version);
        oleSRUSearchRetrieveResponse.setNumberOfRecords(new Long(0));
        if (startRecords == 0 || operation==null || version==null || query==null || !version.equals(OleSRUConstants.SRU_VERSION)) {
            OleSRUDiagnostics oleSRUDiagnostics = new OleSRUDiagnostics();
            List<OleSRUDiagnostic> oleSRUDiagnosticList = new ArrayList<OleSRUDiagnostic>();
            OleSRUDiagnostic oleSRUDiagnostic;
            oleSRUDiagnostic = new OleSRUDiagnostic();
           // oleSRUDiagnostic.setUri("info:srw/diagnostic/1/38");
            if(startRecords == 0){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/6");
                oleSRUDiagnostic.setDetails(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.UN_SUPPORTED_PARAM_VALUE));
                oleSRUDiagnostic.setMessage(OleSRUConstants.START_RECORD);
            }
            if(operation==null){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/7");
                oleSRUDiagnostic.setDetails(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MANDATORY));
                oleSRUDiagnostic.setMessage(OleSRUConstants.OperationType);
            }
            if(version==null){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/7");
                oleSRUDiagnostic.setDetails(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MANDATORY));

                if(oleSRUDiagnostic.getMessage()!=null)
                    oleSRUDiagnostic.setMessage(oleSRUDiagnostic.getMessage()+","+OleSRUConstants.VERSION);
                else
                    oleSRUDiagnostic.setMessage(OleSRUConstants.VERSION);
            }
            if(query==null){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/7");
                oleSRUDiagnostic.setDetails(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MANDATORY));
                if(oleSRUDiagnostic.getMessage()!=null)
                    oleSRUDiagnostic.setMessage(oleSRUDiagnostic.getMessage()+","+OleSRUConstants.QUERY);
                else
                    oleSRUDiagnostic.setMessage(OleSRUConstants.QUERY);
            }
            if(version!=null && !version.equals(OleSRUConstants.SRU_VERSION)){
                oleSRUDiagnostic.setUri("info:srw/diagnostic/1/5");
                oleSRUDiagnostic.setDetails(version+" "+ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.UNSUPPORT_VERSION));
                if(oleSRUDiagnostic.getMessage()!=null)  {
                    oleSRUSearchRetrieveResponse.setVersion((OleSRUConstants.SRU_VERSION));
                    oleSRUDiagnostic.setMessage(oleSRUDiagnostic.getMessage()+","+OleSRUConstants.SRU_VERSION);
                }else{
                    oleSRUSearchRetrieveResponse.setVersion((OleSRUConstants.SRU_VERSION));
                    oleSRUDiagnostic.setMessage("Supported "+OleSRUConstants.VERSION + ":"+OleSRUConstants.SRU_VERSION);
                }
            }
            oleSRUDiagnosticList.add(oleSRUDiagnostic);

            oleSRUDiagnostics.setOleSRUDiagnosticList(oleSRUDiagnosticList);
            oleSRUSearchRetrieveResponse.setOleSRUDiagnostics(oleSRUDiagnostics);
            OleSRUOpacXMLResponseHandler oleSRUOpacXMLResponseHandler = new OleSRUOpacXMLResponseHandler();
           return  oleSRUOpacXMLResponseHandler.toXML(oleSRUSearchRetrieveResponse,recordSchema);
        }  else {
          return  oleSRUWebService.getOleSRUResponse(requestMap);
        }
          //To change body of implemented methods use File | Settings | File Templates.
    }

    public Map getReqParameters(String operation, String query, String version, String startRecord, String maximumRecords, String recordPacking, String recordSchema,String sortKeys,String recordXPath,String resultSetTTL,String styleSheet) {
        HashMap reqParamMap = new HashMap();
        reqParamMap.put(OleSRUConstants.OperationType, operation);
        reqParamMap.put(OleSRUConstants.VERSION, version);
        if (query != null) {
            if (query.startsWith("cql.serverChoice")) {
                String[] querySplit = query.split("=");
                query = querySplit[1];
            }
            reqParamMap.put(OleSRUConstants.QUERY, query);
        }
        if (startRecord != null && !"".equals(startRecord) ){
            int startRecords = Integer.parseInt(startRecord);
            reqParamMap.put(OleSRUConstants.START_RECORD, startRecords - 1);
        } else
            reqParamMap.put(OleSRUConstants.START_RECORD, Integer.parseInt(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.STARTRECORD)));

        if (maximumRecords != null && !"".equals(maximumRecords)) {
            int maxRecord = Integer.parseInt(maximumRecords);
            reqParamMap.put(OleSRUConstants.MAXIMUM_RECORDS, maxRecord);

        } else
            reqParamMap.put(OleSRUConstants.MAXIMUM_RECORDS, Integer.parseInt(ConfigContext.getCurrentContextConfig().getProperty(OleSRUConstants.MAXRECORD)));

        if (recordPacking != null && !"".equals(recordPacking)) {
            reqParamMap.put(OleSRUConstants.RECORD_PACKING, recordPacking);
        } else
            reqParamMap.put(OleSRUConstants.RECORD_PACKING, OleSRUConstants.RECORD_PACK_XML);

        if (sortKeys != null && !"".equals(sortKeys)) {
            reqParamMap.put(OleSRUConstants.SORTKEYS, sortKeys);
        } else
            reqParamMap.put(OleSRUConstants.SORTKEYS, OleSRUConstants.TITLE_SORT_KEYS);

        if (styleSheet != null) {
            reqParamMap.put(OleSRUConstants.STYLE_SHEET, styleSheet);
        }

        if (recordSchema != null  && !"".equals(recordSchema)) {
            if (OleSRUConstants.DC_RECORD_SCHEMA.equals((recordSchema)))
                reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.DC_RECORD_SCHEMA);
            else if (OleSRUConstants.MARC_RECORD_SCHEMA.equals((recordSchema))) {
                reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.MARC_RECORD_SCHEMA);
            } else
                reqParamMap.put(OleSRUConstants.RECORD_SCHEMA,recordSchema);
        } else {
            reqParamMap.put(OleSRUConstants.RECORD_SCHEMA, OleSRUConstants.MARC_RECORD_SCHEMA);


        }
        return reqParamMap;

    }



}
