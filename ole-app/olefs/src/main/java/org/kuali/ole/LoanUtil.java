

package org.kuali.ole;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * LoanUtil supports to load Loan.properties file
 */
public class LoanUtil {
    private static final Logger LOG = Logger.getLogger(LoanUtil.class);
    private static LoanUtil loanUtil = new LoanUtil();
    private String OLE_PROPERTIES_LOAN = "Loan.properties";
    private String environment;

    public static LoanUtil getLoanUtil() {
        return loanUtil;
    }

    private Properties props;

    /**
     * This constructor load the project.properties file
     */
    private LoanUtil() {
        props = new Properties();
        try {
            props.load(getClass().getResourceAsStream(OLE_PROPERTIES_LOAN));
        } catch (Exception e) {
            LOG.error("Unable to load the project.properties file" + e.getMessage());
            LOG.info("Going to attempt to load from the locations set for loan.properties.home");
        }
        String propsDir = System.getProperty("env.properties.home");
        String fileSeparator = System.getProperty("file.separator");
        File userPropsFile = new File(propsDir + fileSeparator + OLE_PROPERTIES_LOAN);
        if (userPropsFile.exists()) {
            try {
                props.load(new FileInputStream(userPropsFile));
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }

    /**
     *  This method returns value of the key.
     * @param key
     * @return String
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }
}
