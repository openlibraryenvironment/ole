package org.kuali.ole.docstore.utility;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.ole.docstore.discovery.util.DiscoveryEnvUtil;
import org.kuali.ole.docstore.indexer.solr.IndexerService;
import org.kuali.ole.docstore.util.DocStoreEnvUtil;
import org.kuali.ole.repository.DeleteManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class DocumentStoreMaintenance {
    DeleteManager deleteManager = new DeleteManager();
    IndexerService indexerService = null;
    DocStoreEnvUtil docStoreEnvUtil = new DocStoreEnvUtil();
    DiscoveryEnvUtil discoveryEnvUtil = new DiscoveryEnvUtil();
    private static Logger logger = LoggerFactory.getLogger(DocumentStoreMaintenance.class);
    private static final String PREPARE_FOR_NEW_BUILD = "prepareForNewBuild";
    private static final String CLEANUP_DATA = "cleanupData";
    private static final String RESET_DOCSTORE = "resetDocstore";

    /**
     * Used to rename the folder / file with the current timestamp.
     *
     * @param filePath
     * @param timeStamp
     */
    private void renameFolder(String filePath, String timeStamp) {
        File srcFile = new File(filePath);
        File destFile = new File(filePath + "-" + timeStamp);
        boolean success = srcFile.renameTo(destFile);
        if (success) {
            logger.info("Renamed " + filePath + " as " + destFile.getAbsolutePath());
        } else {
            logger.info("Failed to rename " + filePath + " as " + destFile.getAbsolutePath());
        }

    }

    /**
     * Used to rename the %ole.docstore.home/repository folder with the current timestamp.
     *
     * @param timeStamp
     */
//    private void deleteDocStoreData(String timeStamp) {
//        String docStoreDataFolder = docStoreEnvUtil.getJackrabbitFolderPath();
//        renameFolder(docStoreDataFolder, timeStamp);
//    }

    /**
     * Used to rename the %ole.discovery.home%/solr-data folder with the current timestamp.
     *
     * @param timeStamp
     */
    private void deleteDiscoveryData(String timeStamp) {
        String discoveryDateFilePath = discoveryEnvUtil.getSolrDataFolderPath();
        renameFolder(discoveryDateFilePath, timeStamp);
    }

    /**
     * Used to rename the %ole.docstore.home/Properties folder with the current timestamp.
     *
     * @param timeStamp
     */
    private void deleteDocstoreProperties(String timeStamp) {
        String docStorePropertiesFilePath = docStoreEnvUtil.getDocStorePropertiesFolderPath();
        renameFolder(docStorePropertiesFilePath, timeStamp);
    }

    /**
     * Used to rename the %ole.discovery.home%/solr-config folder with the current timestamp.
     *
     * @param timeStamp
     */
    private void deleteDiscoveryConfig(String timeStamp) {
        String discoceryConfigFilePath = discoveryEnvUtil.getSolrConfigFolderPath();
        renameFolder(discoceryConfigFilePath, timeStamp);
    }

    /**
     * Used to rename the %ole.docstore.home folder with the current timestamp.
     *
     * @param timeStamp
     */
    private void deleteDocstore(String timeStamp) {
        String docStoreRootFilePath = docStoreEnvUtil.getRootFolderPath();
        renameFolder(docStoreRootFilePath, timeStamp);
    }

    /**
     * Used to rename the %ole.discovery.home folder with the current timestamp.
     *
     * @param timeStamp
     */
    private void deleteDiscovery(String timeStamp) {
        String discoveryRootFilePath = discoveryEnvUtil.getRootFolderPath();
        renameFolder(discoveryRootFilePath, timeStamp);
    }

    /**
     * This method will execute if the argument is prepareForNewBuild in the maintenance.command file. In this process
     * %ole.docstore.home%/properties and %ole.discovery.home/solr-config will renamed with the current timestamp at the time of
     * initialization.
     *
     * @param timeStamp
     */

    private void prepareForNewBuild(String timeStamp) {
        deleteDocstoreProperties(timeStamp);
        deleteDiscoveryConfig(timeStamp);

    }

    /**
     * This method will execute if the argument is resetDocStore in the maintenance.command file. In this process
     * %ole.docstore.home%,%ole.docstore.home%/jackrabbit,%ole.discovery.home and %ole.discovery.home/solr-data will renamed with the
     * current timestamp at the time of initialization.
     *
     * @param timeStamp
     */
    private void resetDocStore(String timeStamp) {
//        deleteDocStoreData(timeStamp);
        deleteDiscoveryData(timeStamp);
        deleteDiscovery(timeStamp);
        deleteDocstore(timeStamp);

    }

    /**
     * This method is called at the time of docstore initialization and operations are performed based on the command mentioned in
     * maintenance.command file
     *
     * @param docStoreHome
     * @param discoveryHome
     * @throws IOException
     * @throws URISyntaxException
     */
    public void docStoreMaintenance(String docStoreHome, String discoveryHome) throws IOException {
        docStoreEnvUtil.initPathValues(docStoreHome);
        discoveryEnvUtil.initPathValues(discoveryHome);
        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String maintenanceFilePath = null;
        String maintenanceFileName = null;
        File maintenanceFile = null;
        File folder = new File(docStoreHome);
        File[] listOfFiles = folder.listFiles();
        String maintenanceAction = null;
        if (listOfFiles != null) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    maintenanceFileName = listOfFiles[i].getName();
                    if (maintenanceFileName.equalsIgnoreCase("maintenance.command")) {
                        maintenanceFilePath = listOfFiles[i].getAbsolutePath();
                        maintenanceFile = new File(maintenanceFilePath);
                        maintenanceAction = FileUtils.readFileToString(maintenanceFile);
                        logger.info("maintenanceAction-->" + maintenanceAction);
                        break;
                    }
                }
            }
        }

        if (!StringUtils.isBlank(maintenanceAction)) {
            if (maintenanceAction.equalsIgnoreCase(PREPARE_FOR_NEW_BUILD)) {
                prepareForNewBuild(timeStamp);

            } else if (maintenanceAction.equalsIgnoreCase(CLEANUP_DATA)) {
                // cleanupData();
            } else if (maintenanceAction.equalsIgnoreCase(RESET_DOCSTORE)) {
                resetDocStore(timeStamp);
            }
            if (maintenanceFile != null) {
                maintenanceFile.delete();
            }

        }
    }
}
