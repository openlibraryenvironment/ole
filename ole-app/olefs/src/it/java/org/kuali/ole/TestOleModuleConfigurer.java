package org.kuali.ole;

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
        String files = ConfigContext.getCurrentContextConfig().getProperty("spring.source.files");
        if (testMode) {
            files = files + "," + ConfigContext.getCurrentContextConfig().getProperty("spring.test.files");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("OLE Spring Files Requested.  Returning: " + files);
        }
        return files == null ? Collections.<String>emptyList() : parseFileList(files);
    }

    protected List<String> parseFileList(String files) {
        List<String> parsedFiles = new ArrayList<String>();
        for (String file : Arrays.asList(files.split(","))) {
            String trimmedFile = file.trim();
            if (!trimmedFile.isEmpty()) {
                parsedFiles.add(trimmedFile);
            }
        }

        return parsedFiles;
    }
}