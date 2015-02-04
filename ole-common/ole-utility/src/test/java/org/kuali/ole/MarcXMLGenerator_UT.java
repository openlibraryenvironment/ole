package org.kuali.ole;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/2/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class MarcXMLGenerator_UT {
    public static final Logger LOG = LoggerFactory.getLogger(MarcXMLGenerator_UT.class);

    @Test
    public void testGenerateMarcXML() throws Exception {
        MarcXMLGenerator marcXMLGenerator = new MarcXMLGenerator();
//        URL resource = getClass().getResource("iu.mrc");
        URL resource = getClass().getResource("duke.mrc");
        File file = new File(resource.toURI());

        String marcXMLFileName = marcXMLGenerator.convertRawMarcToXML(file);
        assertNotNull(marcXMLFileName);

        File marcXMLFile = new File(marcXMLFileName);
        String marcXML = new FileUtil().readFile(marcXMLFile);
        LOG.info(marcXML);

        FileUtils.deleteQuietly(marcXMLFile);

    }

    @Test
    public void testGenerateMarcXMLFromRawMarcContent() throws Exception {
        MarcXMLGenerator marcXMLGenerator = new MarcXMLGenerator();
        URL resource = getClass().getResource("iu.mrc");
        File file = new File(resource.toURI());

        String fileContent = new FileUtilForRawMarc().readFile(file);

        String marcContent = marcXMLGenerator.convert(fileContent);
        assertNotNull(marcContent);
        LOG.info(marcContent);
    }
}
