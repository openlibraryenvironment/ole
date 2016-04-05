package org.kuali.ole.spring.batch.processor;

import org.apache.camel.CamelContext;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.loaders.common.FileUtils;
import org.kuali.ole.spring.batch.util.MarcStreamingUtil;


import java.io.File;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 4/4/2016.
 */
public class MarcFileStreamingTest {

    @Test
    public void testStreamingMarcContent() throws Exception {
        OleCamelContext oleCamelContext = OleCamelContext.getInstance();
        assertNotNull(oleCamelContext);
        String tempLocation = System.getProperty("java.io.tmpdir");
        String batchUploadLocation = tempLocation + File.separator + OleNGConstants.DATE_FORMAT.format(new Date());
        String filelocationPath = FileUtils.getFilePath("org/kuali/ole/spring/batch/processor/InvYBP_1124.mrc");
        assertNotNull(filelocationPath);
        File sourceFileLocation = new File(filelocationPath);
        assertTrue(sourceFileLocation.exists());

        File uploadDirectory = null;
        try {
            if(StringUtils.isNotBlank(batchUploadLocation)) {
                uploadDirectory = new File(batchUploadLocation);
                if (!uploadDirectory.exists() || !uploadDirectory.isDirectory()) {
                    uploadDirectory.mkdirs();
                }
                org.apache.commons.io.FileUtils.copyFileToDirectory(sourceFileLocation, uploadDirectory,false);

                CamelContext context = oleCamelContext.getContext();
                context.start();
                new MarcStreamingUtil().addDynamicMarcStreamRoute(oleCamelContext, uploadDirectory.getAbsolutePath(), 20);
                Thread.sleep(3000);
                context.stop();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != uploadDirectory) {
                uploadDirectory.deleteOnExit();
            }
        }


    }
}
