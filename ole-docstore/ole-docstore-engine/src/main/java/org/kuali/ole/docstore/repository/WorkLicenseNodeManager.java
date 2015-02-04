package org.kuali.ole.docstore.repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.kuali.ole.RepositoryManager;
import org.kuali.ole.docstore.DocStoreConstants;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.model.xmlpojo.ingest.AdditionalAttributes;
import org.kuali.ole.docstore.model.xmlpojo.ingest.RequestDocument;
import org.kuali.ole.pojo.OleException;
import org.omg.CORBA.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * User: tirumalesh.b
 * Date: 4/9/12 Time: 1:00 PM
 */
public class WorkLicenseNodeManager
        extends CustomNodeManager
        implements NodeManager {
    private static WorkLicenseNodeManager ourInstance = new WorkLicenseNodeManager();
    private static Logger LOG = LoggerFactory.getLogger(WorkLicenseNodeManager.class);

    public static WorkLicenseNodeManager getInstance() {
        return ourInstance;
    }

    private WorkLicenseNodeManager() {
        super();
        numLevels = 2;
    }

    public String getParentNodePath() {
        return "/work/license";
    }

}
