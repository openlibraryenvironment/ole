package org.kuali.ole.deliver.controller;

import org.junit.Test;
import org.kuali.ole.deliver.bo.OlePatronConfig;
import org.kuali.ole.ingest.FileUtil;

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
public class OlePatronConfigObjectGeneratorFromXML_UT {
    @Test
    public void testBuildKrmsFromFileContentForLoan() throws Exception {
        URL resource = getClass().getResource("patronConfig.xml");
        File file = new File(resource.toURI());
        String patronConfigXML = new FileUtil().readFile(file);

       OlePatronConfigObjectGeneratorFromXML patronConfigObjectGeneratorFromXML =
                new OlePatronConfigObjectGeneratorFromXML();

        OlePatronConfig patronConfig = patronConfigObjectGeneratorFromXML.buildKrmsFromFileContent(patronConfigXML);
        assertNotNull(patronConfig);

    }
}
