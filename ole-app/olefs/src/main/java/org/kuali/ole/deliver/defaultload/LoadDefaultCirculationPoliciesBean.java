package org.kuali.ole.deliver.defaultload;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.FileUtil;
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
 * Date: 10/31/12
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadDefaultCirculationPoliciesBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoadDefaultCirculationPoliciesBean.class);
    protected static final String LOAD_DEFAULT_CIRCULATION_POLICIES_IND = "LOAD_DEFAULT_CIRCULATION_POLICIES_IND";
    protected String fileName;
    protected OleKrmsBuilder circPolicyBuilder;
    protected ParameterService parameterService;

    public List<String> loadDefaultCircPolicies(boolean forceLoad) throws Exception {
        if (forceLoad || parameterService.getParameterValueAsBoolean(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_CIRCULATION_POLICIES_IND, Boolean.TRUE)) {
            LOG.debug("Starting Load of Default Circulation Policies");

            URL resource = getClass().getResource(fileName);
            File file = new File(resource.toURI());
            String fileContent = new FileUtil().readFile(file);
            List<String> policies = circPolicyBuilder.persistKrmsFromFileContent(fileContent);

            LOG.debug("Completed Load of Default Circulation Policies");

            Parameter existingParameter = parameterService.getParameter(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_CIRCULATION_POLICIES_IND);
            if (existingParameter != null) {
                Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
                updatedParameter.setValue("N");
                parameterService.updateParameter(updatedParameter.build());
            } else {
                Parameter.Builder newParameter = Parameter.Builder.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_CIRCULATION_POLICIES_IND, ParameterType.Builder.create("CONFG"));
                newParameter.setDescription("Set to 'Y' to have the application ingest the default circulation policies upon next startup.");
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

    public void setCircPolicyBuilder(OleKrmsBuilder circPolicyBuilder) {
        this.circPolicyBuilder = circPolicyBuilder;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
