package org.kuali.ole.spring.batch.processor;

import org.apache.camel.CamelContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.i18n.Exception;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OleCamelContext;
import org.kuali.ole.constants.OleNGConstants;
import org.kuali.ole.loaders.common.FileUtils;
import org.kuali.ole.oleng.batch.process.model.BatchJobDetails;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.spring.batch.util.MarcStreamingUtil;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 4/4/2016.
 */
public class MarcFileStreamingTest {

    @Mock
    BatchProcessTxObject mockBatchProcessTxObject;

    @Mock
    BatchFileProcessor mockBatchFileProcessor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

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

        Mockito.when(mockBatchProcessTxObject.getBatchFileProcessor()).thenReturn(mockBatchFileProcessor);

        File uploadDirectory = null;
        try {
            if(StringUtils.isNotBlank(batchUploadLocation)) {
                uploadDirectory = new File(batchUploadLocation);
                if (!uploadDirectory.exists() || !uploadDirectory.isDirectory()) {
                    uploadDirectory.mkdirs();
                }
                org.apache.commons.io.FileUtils.copyFileToDirectory(sourceFileLocation, uploadDirectory,false);


                JSONObject resposne = new JSONObject();
                resposne.put("status", "success");
                Mockito.when(mockBatchFileProcessor.processBatch(uploadDirectory, OleNGConstants.MARC, "1","",new BatchJobDetails())).thenReturn(resposne);

                CamelContext context = oleCamelContext.getContext();
                context.start();
                new MarcStreamingUtil().addDynamicMarcStreamRoute(oleCamelContext, uploadDirectory.getAbsolutePath(), 20, mockBatchProcessTxObject);
                Thread.sleep(3000);
                context.stop();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        } finally {
            if(null != uploadDirectory) {
                uploadDirectory.deleteOnExit();
            }
        }


    }
}
