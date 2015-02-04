package org.kuali.ole.deliver.defaultload;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.KrmsBuilder;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: srinivasane
 * Date: 10/31/12
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadDefaultLicensesBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoadDefaultLicensesBean.class);
    protected static final String LOAD_DEFAULT_LICENSES_IND = "LOAD_DEFAULT_LICENSES_IND";
    protected String fileName;
    protected KrmsBuilder licenseBuilder;
    protected ParameterService parameterService;

    public List<String> loadDefaultLicenses(boolean forceLoad) throws Exception {
        if (forceLoad || parameterService.getParameterValueAsBoolean(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_LICENSES_IND, Boolean.TRUE)) {
            LOG.debug("Starting Load of Default Licenses");

            URL resource = getClass().getResource(fileName);
            File file = new File(resource.toURI());
            String fileContent = new FileUtil().readFile(file);
            List<String> policies = licenseBuilder.persistKrmsFromFileContent(fileContent);

            LOG.debug("Completed Load of Default Licenses");

            Parameter existingParameter = parameterService.getParameter(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_LICENSES_IND);
            if (existingParameter != null) {
                Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
                updatedParameter.setValue("N");
                parameterService.updateParameter(updatedParameter.build());
            } else {
                Parameter.Builder newParameter = Parameter.Builder.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_LICENSES_IND, ParameterType.Builder.create("CONFG"));
                newParameter.setDescription("Set to 'Y' to have the application ingest the default Licenses upon next startup.");
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

    public void setLicenseBuilder(KrmsBuilder licenseBuilder) {
        this.licenseBuilder = licenseBuilder;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
