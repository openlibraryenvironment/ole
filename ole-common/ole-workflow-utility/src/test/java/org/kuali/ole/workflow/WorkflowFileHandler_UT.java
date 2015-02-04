package org.kuali.ole.workflow;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.junit.Test;
import org.kuali.ole.workflow.WorfklowFileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 12/16/12
 * Time: 8:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class WorkflowFileHandler_UT {

    public static final Logger LOG = LoggerFactory.getLogger(WorkflowFileHandler_UT.class);

    @Test
    public void processWorkflows() throws Exception {
        WorfklowFileHandler worfklowFileHandler = new WorfklowFileHandler();
        worfklowFileHandler.setWorkflowXMLSrcDir("org/kuali/ole/workflow");

        String workflowXMLDestDir = System.getProperty("user.home")
                + System.getProperty("file.separator")
                + "kuali/main/local/olefs-webapp/workflow/pending";
        worfklowFileHandler.setWorkflowXMLDestDir(workflowXMLDestDir);
        worfklowFileHandler.execute();
        File destDir = new File(workflowXMLDestDir);
        assertNotNull(destDir);
        File[] files = destDir.listFiles();
        assertTrue(files.length > 0);
        for (int i = 0; i < files.length; i++) {
            LOG.info(files[i].getName());
        }
        FileUtils.deleteDirectory(destDir);
    }
}
