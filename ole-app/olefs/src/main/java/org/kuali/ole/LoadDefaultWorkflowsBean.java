package org.kuali.ole;


import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.workflow.WorfklowFileHandler;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

/**
 *
 */
public class LoadDefaultWorkflowsBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoadDefaultWorkflowsBean.class);
    protected static final String LOAD_DEFAULT_WORKFLOW_IND = "LOAD_DEFAULT_WORKFLOW_IND";
    protected ParameterService parameterService;
    protected WorfklowFileHandler workflowFileHandler;

    public void unpackWorkflows( boolean forceLoad ) {
        if ( forceLoad || parameterService.getParameterValueAsBoolean(OLEConstants.OLE_NMSPC,  OLEConstants.OLE_CMPNT,  LOAD_DEFAULT_WORKFLOW_IND, Boolean.TRUE) ) {

            LOG.info("Starting Load of Default Workflows");
            workflowFileHandler.execute();
            LOG.info("Completed Load of Default Workflows");

            Parameter existingParameter = parameterService.getParameter(OLEConstants.OLE_NMSPC,  OLEConstants.OLE_CMPNT,  LOAD_DEFAULT_WORKFLOW_IND);
            if ( existingParameter != null ) {
                Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
                updatedParameter.setValue("N");
                parameterService.updateParameter(updatedParameter.build());
            } else {
                Parameter.Builder newParameter = Parameter.Builder.create(OLEConstants.APPL_ID, OLEConstants.OLE_NMSPC,  OLEConstants.OLE_CMPNT,  LOAD_DEFAULT_WORKFLOW_IND, ParameterType.Builder.create("CONFG"));
                newParameter.setDescription( "Set to 'Y' to have the application ingest the default circulation policies upon next startup." );
                newParameter.setValue("N");
                parameterService.createParameter(newParameter.build());
            }
        }
    }

    public void setWorkflowFileHandler(WorfklowFileHandler workflowFileHandler) {
        this.workflowFileHandler = workflowFileHandler;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
