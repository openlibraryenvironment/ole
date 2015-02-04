package org.kuali.ole;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

/**
* Created with IntelliJ IDEA.
* User: peris
* Date: 10/31/12
* Time: 4:54 PM
* To change this template use File | Settings | File Templates.
*/
@Transactional
public class LoadDefaultWorkflowsBean_IT extends SpringBaseTestCase {

//    TODO: This test can be a simple test to unpack the xml files and verify they got unpacked. No need to get it
//    from spring.
    @Test
    @Transactional
    public void unpackWorkflowXMLs() throws Exception {
        String userHome = System.getProperty("user.home");
        String folderPathSeperator = System.getProperty("file.separator");
        StringBuilder destPath = new StringBuilder();
        destPath.append(userHome).append(folderPathSeperator).append("kuali")
                .append(folderPathSeperator).
                append("main")
                .append(folderPathSeperator)
                .append("dev")
                .append(folderPathSeperator)
                .append("olerice2")
                .append(folderPathSeperator)
                .append("workflow")
                .append(folderPathSeperator)
                .append("pending")
                .append(folderPathSeperator);
        FileUtils.deleteDirectory(new File(destPath.toString()));
        LoadDefaultWorkflowsBean loadDefaultWorkflowsBean = GlobalResourceLoader.getService("loadDefaultWorkflowsBean");
        loadDefaultWorkflowsBean.unpackWorkflows(false);
    }
}
