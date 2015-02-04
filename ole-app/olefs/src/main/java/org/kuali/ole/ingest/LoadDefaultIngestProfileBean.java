package org.kuali.ole.ingest;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.krms.builder.OleKrmsBuilder;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/30/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadDefaultIngestProfileBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoadDefaultIngestProfileBean.class);
    protected static final String LOAD_DEFAULT_INGEST_POLICIES_IND = "LOAD_DEFAULT_INGEST_POLICIES_IND";
    protected String fileName;
    protected OleKrmsBuilder oleKrmsBuilder;
    protected ParameterService parameterService;

    public List<String> loadDefaultIngestProfile( boolean forceLoad ) throws Exception {
        if ( forceLoad || parameterService.getParameterValueAsBoolean(OLEConstants.DLVR_NMSPC,  OLEConstants.DLVR_CMPNT,  LOAD_DEFAULT_INGEST_POLICIES_IND, Boolean.TRUE) ) {
            LOG.info("Starting Load of Default Ingest Profiles");
            URL resource = getClass().getResource(fileName);
            File file = new File(resource.toURI());
            String fileContent = new FileUtil().readFile(file);
            List<String> policies= oleKrmsBuilder.persistKrmsFromFileContent(fileContent);
            LOG.info("Completed Load of Default Ingest Profiles");

            Parameter existingParameter = parameterService.getParameter(OLEConstants.DLVR_NMSPC,  OLEConstants.DLVR_CMPNT,  LOAD_DEFAULT_INGEST_POLICIES_IND);
            if ( existingParameter != null ) {
                Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
                updatedParameter.setValue("N");
                parameterService.updateParameter(updatedParameter.build());
            } else {
                Parameter.Builder newParameter = Parameter.Builder.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC,  OLEConstants.DLVR_CMPNT,  LOAD_DEFAULT_INGEST_POLICIES_IND, ParameterType.Builder.create("CONFG"));
                newParameter.setDescription( "Set to 'Y' to have the application ingest the default circulation policies upon next startup." );
                newParameter.setValue("N");
                parameterService.createParameter(newParameter.build());
            }

            return policies;
        }
        return Collections.emptyList();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setOleKrmsBuilder(OleKrmsBuilder oleKrmsBuilder) {
        this.oleKrmsBuilder = oleKrmsBuilder;
    }
    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
