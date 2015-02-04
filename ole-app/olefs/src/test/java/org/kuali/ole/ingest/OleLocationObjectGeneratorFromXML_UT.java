package org.kuali.ole.ingest;

import org.junit.Test;
import org.kuali.ole.ingest.pojo.OleLocationGroup;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;


public class OleLocationObjectGeneratorFromXML_UT {
    @Test
    public void testBuildLocationFromFileContent() throws Exception {
        URL resource = getClass().getResource("DefaultLibraryLocations.xml");
        File file = new File(resource.toURI());
        String locationXML = new FileUtil().readFile(file);
        OleLocationObjectGeneratorFromXML oleLocationObjectGeneratorFromXML =
                new OleLocationObjectGeneratorFromXML();

        OleLocationGroup location = oleLocationObjectGeneratorFromXML.buildLocationFromFileContent(locationXML);
        assertNotNull(location);
    }
}
