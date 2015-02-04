package org.kuali.ole.docstore;

/**
 * Class for defining project-wide constants.
 * User: tirumalesh.b
 * Date: 24/2/12 Time: 1:16 PM
 */
public class DocStoreConstants {
    // For DEV/PROD general environment.
    public static final String DOC_STORE_HOME = "/opt/docstore";
    public static final String DOC_STORE_PROP_HOME = DOC_STORE_HOME + "/properties";

    // For unit tests.
    public static final String TEST_DOC_STORE_HOME = System.getProperty("user.home") + "/kuali/main/local/docstore-test";
    public static final String TEST_DOC_STORE_PROP_HOME = TEST_DOC_STORE_HOME + "/properties";

    public static boolean isVersioningEnabled = false;

    public static final String DOCSTORE_SETTINGS_DIR_PATH = "/opt/docstore";
    public static final String RESOURCES_DIR_NAME = "ds-resources";

    // For uuid check web service while deleting records
    public static final String LINK = "link";
    public static final String REQUISITION = "REQUISITION";
    public static final String PURCHASE_ORDER = "PURCHASE ORDER";
    public static final String PAYMENT_REQUEST = "PAYMENT REQUEST";
    public static final String LINE_ITEM = "LINE ITEM";
    public static final String COPY = "COPY";
    public static final String REQUEST = "REQUEST";
    public static final String LOAN = "LOAN";
    public static final String SERIAL_RECEIVING = "SERIAL RECEIVING";
    public static final String UUID_CHECK_WEB_SERVICE_URL = "uuidCheckServiceURL";
    public static final String UUID_CHECK_WEB_SERVICE_CLASS = "org.kuali.ole.docstore.service.OleUuidCheckWebService";
    public static final String UUID_CHECK_WEB_SERVICE = "oleUuidCheckWebService";
    public static final String COMMA = ",";

    //Added for  External Z3950 Bib Import
    public static final String APPL_ID = "KUALI";
    public static final String SELECT_NMSPC = "OLE-DESC";
    public static final String SELECT_CMPNT = "Describe";
    public static final String LEFT_PADDING_SIZE = "editor.left.padding.size";
}
