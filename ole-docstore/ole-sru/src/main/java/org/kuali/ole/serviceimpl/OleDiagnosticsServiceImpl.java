package org.kuali.ole.serviceimpl;

import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostic;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.handler.OleSRUDiagnosticsHandler;
import org.kuali.ole.service.OleDiagnosticsService;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/9/12
 * Time: 6:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDiagnosticsServiceImpl implements OleDiagnosticsService {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    public OleSRUDiagnosticsHandler oleSRUDiagnosticsHandler;
    private Config currentContextConfig;

    public OleDiagnosticsServiceImpl() {
        oleSRUDiagnosticsHandler = new OleSRUDiagnosticsHandler();
    }

    /**
     * this method will return the diagnostic response(error response)
     *
     * @param errorMessage
     * @return xml as a OleSRUDiagnostics
     */
    public OleSRUDiagnostics getDiagnosticResponse(String errorMessage) {
        LOG.info("Inside getDiagnosticResponse Method");
        OleSRUDiagnostic oleSRUDiagnostic = new OleSRUDiagnostic();
        if(!errorMessage.equals("")){
            oleSRUDiagnostic.setMessage(errorMessage);
        if(errorMessage.equals(getCurrentContextConfig().getProperty(OleSRUConstants.INVALID_OperationType))){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/4");
            oleSRUDiagnostic.setDetails(OleSRUConstants.OperationType);
        }else if(errorMessage.equals(getCurrentContextConfig().getProperty(OleSRUConstants.INVALID_RECORD_PACKING))){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/71");
            oleSRUDiagnostic.setDetails(OleSRUConstants.RECORD_PACKING);
        }else if(errorMessage.equals(getCurrentContextConfig().getProperty(OleSRUConstants.INVALID_RECORD_SCHEMA))){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/66");
            oleSRUDiagnostic.setDetails(OleSRUConstants.RECORD_SCHEMA);
        }else if(errorMessage.equals(getCurrentContextConfig().getProperty(OleSRUConstants.INVALID_QUERY_DIAGNOSTIC_MSG))){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/10");
            oleSRUDiagnostic.setDetails(OleSRUConstants.QUERY);
        }else if(errorMessage.equals("Local Id Value Should be a Number")){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/8");
            oleSRUDiagnostic.setDetails(OleSRUConstants.QUERY);
        }else if(errorMessage.equals(getCurrentContextConfig().getProperty(OleSRUConstants.SERVER_DIAGNOSTIC_MSG))){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/2");
        }else if(errorMessage.equals(getCurrentContextConfig().getProperty(OleSRUConstants.START_RECORD_UNMATCH))){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/61");
            oleSRUDiagnostic.setDetails(OleSRUConstants.START_RECORD);
        } else if(errorMessage.equals(getCurrentContextConfig().getProperty(OleSRUConstants.NORECORDS_DIAGNOSTIC_MSG))){
            oleSRUDiagnostic.setUri("info:srw/diagnostic/1/65");
          //  oleSRUDiagnostic.setDetails(OleSRUConstants.START_RECORD);
        }
        }
        OleSRUDiagnostics oleSRUDiagnostics = new OleSRUDiagnostics();
        List<OleSRUDiagnostic> oleSRUDiagnosticList = new ArrayList<OleSRUDiagnostic>();
        oleSRUDiagnosticList.add(oleSRUDiagnostic);
        oleSRUDiagnostics.setOleSRUDiagnosticList(oleSRUDiagnosticList);
        return oleSRUDiagnostics;
    }

    private Config getCurrentContextConfig() {
        if (null == currentContextConfig) {
            currentContextConfig = ConfigContext.getCurrentContextConfig();
        }
        return currentContextConfig;
    }

    public void setCurrentContextConfig(Config currentContextConfig) {
        this.currentContextConfig = currentContextConfig;
    }
}
