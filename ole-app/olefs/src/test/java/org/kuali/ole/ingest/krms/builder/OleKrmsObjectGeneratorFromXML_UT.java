package org.kuali.ole.ingest.krms.builder;

import org.junit.Test;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.ingest.krms.pojo.OleKrms;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 7/2/12
 * Time: 11:21 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleKrmsObjectGeneratorFromXML_UT {
    @Test
    public void testBuildKrmsFromFileContentForLoan() throws Exception {
        URL resource = getClass().getResource("deliver.xml");
        File file = new File(resource.toURI());
        String krmsXML = new FileUtil().readFile(file);

       OleKrmsObjectGeneratorFromXML krmsObjectGeneratorFromXML =
                new OleKrmsObjectGeneratorFromXML();

        OleKrms krms = krmsObjectGeneratorFromXML.buildKrmsFromFileContent(krmsXML);
        assertNotNull(krms);

    }
}
