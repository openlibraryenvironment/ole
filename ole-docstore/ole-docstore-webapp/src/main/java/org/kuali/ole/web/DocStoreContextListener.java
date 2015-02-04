package org.kuali.ole.web;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.discovery.util.DiscoveryEnvUtil;
import org.kuali.ole.docstore.util.DocStoreEnvUtil;
import org.kuali.ole.docstore.utility.DocumentStoreMaintenance;
import org.kuali.ole.utility.Constants;

/**
 *
 */
public class DocStoreContextListener implements ServletContextListener {
    DocStoreEnvUtil docStoreEnvUtil = new DocStoreEnvUtil();
    DiscoveryEnvUtil discoveryEnvUtil = new DiscoveryEnvUtil();
    DocumentStoreMaintenance documentStoreMaintenance = new DocumentStoreMaintenance();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            docStoreEnvUtil.initEnvironment();
            discoveryEnvUtil.initEnvironment();
            String docstoreHome = System.getProperty(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY);
            String discoveryHome = System.getProperty(Constants.OLE_DISCOVERY_HOME_SYSTEM_PROPERTY);
            validate(docstoreHome, discoveryHome);
            documentStoreMaintenance.docStoreMaintenance(docstoreHome, discoveryHome);
        } catch (IOException e) {
            // If the system can't be initialized properly there is no point in continuing
            // Always throw an exception here
            throw new IllegalStateException(e);
        }
    }

    protected void validate(String docstoreHome, String discoveryHome) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isBlank(docstoreHome)) {
            sb.append(Constants.OLE_DOCSTORE_HOME_SYSTEM_PROPERTY + " cannot be blank. ");
        }
        if (StringUtils.isBlank(docstoreHome)) {
            sb.append(Constants.OLE_DISCOVERY_HOME_SYSTEM_PROPERTY + " cannot be blank.");
        }
        if (sb.length() != 0) {
            throw new IllegalStateException(sb.toString());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public DocStoreEnvUtil getDocStoreEnvUtil() {
        return docStoreEnvUtil;
    }

    public void setDocStoreEnvUtil(DocStoreEnvUtil docStoreEnvUtil) {
        this.docStoreEnvUtil = docStoreEnvUtil;
    }
}
