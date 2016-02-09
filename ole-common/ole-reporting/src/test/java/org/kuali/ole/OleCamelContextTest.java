package org.kuali.ole;

import org.apache.camel.Processor;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 2/9/16.
 */
public class OleCamelContextTest {

    @Test
    public void getCamelContext() throws Exception {
        OleCamelContext oleCamelContext = OleCamelContext.getInstance();
        assertNotNull(oleCamelContext);
        String filePath = System.getProperty("java.io.tmpdir");
        String fileName1 = "testReport1.txt";
        String fileName2 = "testReport2.txt";
        oleCamelContext.addRoutes("seda:q1", "file:" + filePath +"?fileName="+fileName1, new ArrayList<Processor>());
        oleCamelContext.addRoutes("seda:q2", "file:" + filePath +"?fileName="+fileName2,new ArrayList<Processor>());

        assertTrue(oleCamelContext.getContext().getRoutes().size() == 2);

    }

}