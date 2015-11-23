package org.kuali.ole.deliver.defaultload;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.util.OleFileUtils;
import org.kuali.rice.core.api.config.property.ConfigContext;

import java.io.File;
import java.io.IOException;

/**
 * Created by SheikS on 11/22/2015.
 */
public class CopyDefaultRuleFilesBean {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(CopyDefaultRuleFilesBean.class);
    protected static final String COPY_DEFAULT_RULE_FILES_IND = "COPY_DEFAULT_RULE_FILES_IND";

    public void copyDefaultRuleFiles() throws IOException {
        LOG.info("Starting copy rule files");
        File ruleSourceDirectory = OleFileUtils.getInstance().getFile(getRuleSourcePath());
        File ruleDestinationDirectory = new File(getRuleDestinationPath());
        if(ruleSourceDirectory.exists()){
            if(!ruleDestinationDirectory.exists()) {
                ruleDestinationDirectory.mkdirs();
            }
            FileUtils.copyDirectory(ruleSourceDirectory,ruleDestinationDirectory);
            LOG.info("Completed copy rule files");
        } else {
            throw new RuntimeException("Invalid rule source directory");
        }
    }

    public String getRuleDestinationPath() {
        return ConfigContext.getCurrentContextConfig().getProperty("rules.directory");
    }

    public String getRuleSourcePath() {
        return ConfigContext.getCurrentContextConfig().getProperty("rules.source.directory");
    }
}
