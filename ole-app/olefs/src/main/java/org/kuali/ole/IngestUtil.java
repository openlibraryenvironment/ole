package org.kuali.ole;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * LoanUtil supports to load Loan.properties file
 */
public class IngestUtil {
    private static final Logger LOG = Logger.getLogger(IngestUtil.class);
    private static IngestUtil ingestUtil = new IngestUtil();
    private String OLE_PROPERTIES_INGEST = "Ingest.properties";
    private String environment;

    public static IngestUtil getIngestUtil() {
        return ingestUtil;
    }

    private Properties props;


    private IngestUtil() {
        props = new Properties();
        try {
            props.load(getClass().getResourceAsStream(OLE_PROPERTIES_INGEST));
        } catch (Exception e) {
            LOG.error("Unable to load the project.properties file" + e.getMessage()+"   VALUE: "+e);
            LOG.info("Going to attempt to load from the locations set for loan.properties.home");
        }
        String propsDir = System.getProperty("env.properties.home");
        String fileSeparator = System.getProperty("file.separator");
        File userPropsFile = new File(propsDir + fileSeparator + OLE_PROPERTIES_INGEST);
        if (userPropsFile.exists()) {
            try {
                props.load(new FileInputStream(userPropsFile));
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }


    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
