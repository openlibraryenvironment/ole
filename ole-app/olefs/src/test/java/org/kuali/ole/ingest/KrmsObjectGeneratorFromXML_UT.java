package org.kuali.ole.ingest;

import org.junit.Test;
import org.kuali.ole.ingest.pojo.Krms;

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
public class KrmsObjectGeneratorFromXML_UT {
    @Test
    public void testBuildKrmsFromFileContentForLoan() throws Exception {
        URL resource = getClass().getResource("Loan_Patron.xml");
        File file = new File(resource.toURI());
        String krmsXML = new FileUtil().readFile(file);

        KrmsObjectGeneratorFromXML krmsObjectGeneratorFromXML =
                new KrmsObjectGeneratorFromXML();

        Krms krms = krmsObjectGeneratorFromXML.buildKrmsFromFileContent(krmsXML);
        assertNotNull(krms);

    }
    @Test
    public void testBuildKrmsFromFileContentForLicense() throws Exception {
        URL resource = getClass().getResource("license.xml");
        File file = new File(resource.toURI());
        String krmsXML = new FileUtil().readFile(file);

        KrmsObjectGeneratorFromXML krmsObjectGeneratorFromXML =
                new KrmsObjectGeneratorFromXML();

        Krms krms = krmsObjectGeneratorFromXML.buildKrmsFromFileContent(krmsXML);
        assertNotNull(krms);

    }
}
