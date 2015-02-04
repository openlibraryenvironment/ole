package org.kuali.ole.ingest;

import org.junit.Test;
import org.kuali.ole.ingest.pojo.OlePatron;
import org.kuali.ole.ingest.pojo.OlePatronGroup;

import java.io.File;
import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertNotNull;


public class OlePatronRecordHandler_UT {
    @Test
    public void testBuildPatronFromFileContent() throws Exception {
        URL resource = getClass().getResource("samplePatronRecord.xml");
        File file = new File(resource.toURI());
        String patronXML = new FileUtil().readFile(file);
        OlePatronRecordHandler olePatronRecordHandler =
                new OlePatronRecordHandler();

        OlePatronGroup patron = olePatronRecordHandler.buildPatronFromFileContent(patronXML);
        assertNotNull(patron);
    }

    @Test
    public void testToXML() throws Exception {
        URL resource = getClass().getResource("samplePatronRecord.xml");
        File file = new File(resource.toURI());
        String patronXML = new FileUtil().readFile(file);
        OlePatronRecordHandler olePatronRecordHandler =
                new OlePatronRecordHandler();

        OlePatronGroup patron = olePatronRecordHandler.buildPatronFromFileContent(patronXML);
        assertNotNull(patron);
        List<OlePatron> olePatron = patron.getPatronGroup();
        String toXML = olePatronRecordHandler.toXML(olePatron);
        assertNotNull(toXML);
    }

}
