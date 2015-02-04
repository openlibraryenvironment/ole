package org.kuali.ole.converter;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class MarcXMLConverter_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(MarcXMLConverter_UT.class);


    @Test
    public void testConvertMarcXML() throws Exception {
        MarcXMLConverter marcXMLConverter = new MarcXMLConverter();
        marcXMLConverter.generateMarcBean(new BibliographicRecord());
        URL resource = getClass().getResource("/org/kuali/ole/duke.mrc");
        File file = new File(resource.toURI());
        String marcXMLFileName = marcXMLConverter.convertRawMarcToXML(file);
        LOG.info("file:" + marcXMLFileName);
        assertNotNull(marcXMLFileName);
        File marcXMLFile = new File(marcXMLFileName);
        String marcXML = new FileUtil().readFile(marcXMLFile);
        LOG.info(marcXML);
        FileUtils.deleteQuietly(marcXMLFile);

    }

}
