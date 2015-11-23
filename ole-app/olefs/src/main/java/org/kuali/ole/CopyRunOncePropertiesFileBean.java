package org.kuali.ole;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.util.OleFileUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

/**
 * Created by SheikS on 11/22/2015.
 */
public class CopyRunOncePropertiesFileBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CopyRunOncePropertiesFileBean.class);
    protected static final String DB_RESET = "DB_RESET";
    protected ParameterService parameterService;

    public void copyRunOncePropertiesFile( boolean forceLoad ) throws IOException {
        if ( forceLoad || parameterService.getParameterValueAsBoolean(org.kuali.ole.sys.OLEConstants.OLE_NMSPC,  org.kuali.ole.sys.OLEConstants.OLE_CMPNT,
                DB_RESET, Boolean.TRUE) ) {

            LOG.info("Starting copy runonce.properties");
            String separator = File.separator;
            File sourceFilePath = OleFileUtils.getInstance().getFile("org" + separator + "kuali" + separator + "ole" + separator + "runonce.properties");
            String projectHomeDirectory = getProjectHome();
            File projectHome = new File(projectHomeDirectory);

            FileUtils.copyFileToDirectory(sourceFilePath,projectHome);

            LOG.info("Completed copy runonce.properties");

            // Create or update system parameter after copying file.
            createOrUpdateParameter();
        }
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }

    public String getProjectHome() {
        return ConfigContext.getCurrentContextConfig().getProperty("project.home");
    }

    public void createOrUpdateParameter() {
        Parameter existingParameter = parameterService.getParameter(org.kuali.ole.sys.OLEConstants.OLE_NMSPC,  org.kuali.ole.sys.OLEConstants.OLE_CMPNT,
                DB_RESET);
        if ( existingParameter != null ) {
            Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
            updatedParameter.setValue("N");
            parameterService.updateParameter(updatedParameter.build());
        } else {
            Parameter.Builder newParameter = Parameter.Builder.create(org.kuali.ole.sys.OLEConstants.APPL_ID,
                    org.kuali.ole.sys.OLEConstants.OLE_NMSPC,  org.kuali.ole.sys.OLEConstants.OLE_CMPNT,  DB_RESET, ParameterType.Builder.create("CONFG"));
            newParameter.setDescription( "Set to 'Y' to have the application copy the runonce.properties upon next startup." );
            newParameter.setValue("N");
            parameterService.createParameter(newParameter.build());
        }
    }
}
