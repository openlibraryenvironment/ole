package org.kuali.ole.deliver.defaultload;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.OLEConstants;
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
public class CopyDefaultRuleFilesBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CopyDefaultRuleFilesBean.class);
    protected static final String COPY_DEFAULT_RULE_FILES_IND = "COPY_DEFAULT_RULE_FILES_IND";
    protected ParameterService parameterService;

    public void copyDefaultRuleFiles( boolean forceLoad ) throws IOException {
        if ( forceLoad || parameterService.getParameterValueAsBoolean(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                COPY_DEFAULT_RULE_FILES_IND, Boolean.TRUE) ) {

            LOG.info("Starting copy rule files");
            File ruleSourceDirectory = OleFileUtils.getInstance().getFile(ConfigContext.getCurrentContextConfig().getProperty("rules.source.directory"));
            File ruleDestinationDirectory = new File(ConfigContext.getCurrentContextConfig().getProperty("rules.directory"));
            if(ruleSourceDirectory.exists()){
                if(!ruleDestinationDirectory.exists()) {
                    ruleDestinationDirectory.mkdirs();
                }
                FileUtils.copyDirectory(ruleSourceDirectory,ruleDestinationDirectory);
                LOG.info("Completed copy rule files");

                Parameter existingParameter = parameterService.getParameter(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,
                        COPY_DEFAULT_RULE_FILES_IND);
                if ( existingParameter != null ) {
                    Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
                    updatedParameter.setValue("N");
                    parameterService.updateParameter(updatedParameter.build());
                } else {
                    Parameter.Builder newParameter = Parameter.Builder.create(org.kuali.ole.sys.OLEConstants.APPL_ID,
                            OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,  COPY_DEFAULT_RULE_FILES_IND, ParameterType.Builder.create("CONFG"));
                    newParameter.setDescription( "Set to 'Y' to have the application copy the default rule files upon next startup." );
                    newParameter.setValue("N");
                    parameterService.createParameter(newParameter.build());
                }
            } else {
                throw new RuntimeException("Invalid rule source directory");
            }
        }
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
