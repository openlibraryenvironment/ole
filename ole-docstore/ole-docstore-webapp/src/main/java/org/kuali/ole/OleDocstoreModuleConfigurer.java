package org.kuali.ole;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.discovery.util.DiscoveryEnvUtil;
import org.kuali.ole.docstore.util.DocStoreEnvUtil;
import org.kuali.ole.docstore.utility.DocumentStoreMaintenance;
import org.kuali.ole.utility.Constants;
import org.kuali.rice.core.api.config.module.RunMode;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.framework.config.module.ModuleConfigurer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;


/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 6/13/13
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDocstoreModuleConfigurer extends ModuleConfigurer {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDocstoreModuleConfigurer.class);

    DocStoreEnvUtil docStoreEnvUtil = new DocStoreEnvUtil();
    DiscoveryEnvUtil discoveryEnvUtil = new DiscoveryEnvUtil();
    DocumentStoreMaintenance documentStoreMaintenance = new DocumentStoreMaintenance();

    public OleDocstoreModuleConfigurer() {
        super("OLE-DOCSTORE");
        LOG.info("OLE-Docstore Configurer instantiated");
        setValidRunModes(Arrays.asList(RunMode.LOCAL));
    }

    @Override
    protected void doAdditionalModuleStartLogic() throws Exception {
        LOG.info("*********************************************************");
        LOG.info("OLE Docstore Starting Module");
        LOG.info("*********************************************************");
        super.doAdditionalModuleStartLogic();

        try {
            docStoreEnvUtil.initEnvironment();
            discoveryEnvUtil.initEnvironment();
            String fileSeparator = File.separator;
            File file = new File(discoveryEnvUtil.getSolrConfigDirectoryPath()+fileSeparator+"bib"+fileSeparator+"conf"+fileSeparator+"solrcore.properties");
            file.deleteOnExit();
            try{
                Properties props = new Properties();
                FileOutputStream out = new FileOutputStream(file);
                props.setProperty("db.driver", ConfigContext.getCurrentContextConfig().getProperty("jdbc.driver"));
                props.setProperty("db.url", ConfigContext.getCurrentContextConfig().getProperty("jdbc.url"));
                props.setProperty("db.username", ConfigContext.getCurrentContextConfig().getProperty("jdbc.username"));
                props.setProperty("db.password", ConfigContext.getCurrentContextConfig().getProperty("jdbc.password"));
                props.store(out, null);
                out.close();
            }catch(Exception e){
                e.printStackTrace();
            }
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
    protected void doAdditionalModuleStopLogic() throws Exception {
        LOG.info("*********************************************************");
        LOG.info("OLE Stopping Module");
        LOG.info("*********************************************************");
        super.doAdditionalModuleStopLogic();
    }
}
