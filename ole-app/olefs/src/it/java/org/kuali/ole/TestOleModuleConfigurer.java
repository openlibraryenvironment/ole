package org.kuali.ole;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sheiksalahudeenm on 8/4/15.
 */
public class TestOleModuleConfigurer extends ModuleConfigurer {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(TestOleModuleConfigurer.class);

    protected boolean testMode = false;

    public TestOleModuleConfigurer() {
        super("OLE");
        LOG.info( "TestOLEConfigurer instantiated" );
        setValidRunModes(Arrays.asList(RunMode.LOCAL));
    }

    @Override
    protected void doAdditionalModuleStartLogic() throws Exception {
        LOG.info("*********************************************************");
        LOG.info("Test OLE Module Starting");
        LOG.info("*********************************************************");
        super.doAdditionalModuleStartLogic();
    }

    @Override
    protected void doAdditionalModuleStopLogic() throws Exception {
        LOG.info("*********************************************************");
        LOG.info("Test OLE Module Stopping");
        LOG.info("*********************************************************");
        super.doAdditionalModuleStopLogic();
    }

    @Override
    public List<String> getPrimarySpringFiles() {
        List<String> fileLocationList = new ArrayList<>();
        List<String> fileLocationResponseList = new ArrayList<>();
        String files = ConfigContext.getCurrentContextConfig().getProperty("spring.source.files");
        if (testMode) {
            files = files + "," + ConfigContext.getCurrentContextConfig().getProperty("spring.test.files");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("OLE Spring Files Requested.  Returning: " + files);
        }
        fileLocationList = parseFileList(files);
        if(CollectionUtils.isNotEmpty(fileLocationList)){
            fileLocationResponseList.addAll(fileLocationList);
        }
        files = ConfigContext.getCurrentContextConfig().getProperty("test.spring.source.files");
        fileLocationList = parseFileList(files);
        if(CollectionUtils.isNotEmpty(fileLocationList)){
            fileLocationResponseList.addAll(fileLocationList);
        }
        return fileLocationResponseList;
    }

    protected List<String> parseFileList(String files) {
        List<String> parsedFiles = new ArrayList<String>();
        if(StringUtils.isNotBlank(files)){
            for (String file : Arrays.asList(files.split(","))) {
                String trimmedFile = file.trim();
                if (!trimmedFile.isEmpty()) {
                    parsedFiles.add(trimmedFile);
                }
            }
        }
        return parsedFiles;
    }
}
