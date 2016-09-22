package org.kuali.ole.dsng.rest.controller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.kuali.ole.utility.OleDsNgRestClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 12/23/2015.
 */
public class OleDsNGRestAPIControllerTest {

    @Test
    public void testProcessBibHoldingsItems() throws Exception {
        File file = getFile("org/kuali/ole/dsng/rest/controller/processBibHoldingsItem.json");
        assertNotNull(file);
        String content = FileUtils.readFileToString(file);
        assertTrue(StringUtils.isNotBlank(content));
        OleDsNgRestClient oleDsNgRestClient = new MockOleDsNgRestClient();
        String responseData = oleDsNgRestClient.postData(OleDsNgRestClient.Service.PROCESS_BIB_HOLDING_ITEM, content, OleDsNgRestClient.Format.JSON);
        assertTrue(StringUtils.isNotBlank(responseData));
        System.out.println(responseData);
    }



    public File getFile(String classpathRelativePath)  {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile();
        } catch(Exception e){
            System.out.println("Error : while accessing file "+e);
        }
        return null;
    }

    class MockOleDsNgRestClient extends OleDsNgRestClient {
        @Override
        public String getDsNgBaseUrl() {;
            return "http://localhost:8080/oledocstore";
        }
    }
}