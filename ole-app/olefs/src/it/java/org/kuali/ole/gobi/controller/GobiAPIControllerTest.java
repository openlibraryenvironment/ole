package org.kuali.ole.gobi.controller;

import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.oleng.gobi.controller.OleNgGobiAPIController;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class GobiAPIControllerTest extends OLETestCaseBase {

    @Test
    public void testCreateOrder() throws Exception {
        OleNgGobiAPIController gobiAPIController = new OleNgGobiAPIController();
        String body = readFileContent("org/kuali/ole/gobi/request/UnlistedPrintMonograph.xml");
        ResponseEntity<String> order = gobiAPIController.createOrder(body);
        assertNotNull(order);
        System.out.println(order);
    }



    public String readFileContent(String path) throws IOException {
        BufferedReader br=new BufferedReader(new FileReader(getFilePath(path)));
        String line=null;
        String fullContent = "";
        while ((line=br.readLine())!=null)
        {
            fullContent += line;
        }
        return fullContent;
    }

    public String getFilePath(String classpathRelativePath)  {
        try {
            Resource rsrc = new ClassPathResource(classpathRelativePath);
            return rsrc.getFile().getAbsolutePath();
        } catch(Exception e){
            System.out.println("Error : while accessing file "+e);
        }
        return null;
    }
}