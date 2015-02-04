package org.kuali.ole;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecords;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.BibMarcRecordProcessor;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.ProfileAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 4/9/12
 * Time: 3:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleItemRecordHandler_UT {
    public static final Logger LOG = LoggerFactory.getLogger(OleItemRecordHandler_UT.class);
    //TODO: use BibMarcRecord instead of  BibliographicRecord
    @Test
    public void testGenerateItemXML() throws Exception {
        URL resource = getClass().getResource("iu.mrc");
        File file = new File(resource.toURI());
        MarcXMLGenerator marcXMLGenerator = new MarcXMLGenerator();
        String fileName = marcXMLGenerator.convertRawMarcToXML(file);

        File xmlFile = new File(fileName);

        BibMarcRecords bibliographicRecordCollection =
                new BibMarcRecordProcessor().fromXML(new FileUtil().readFile(xmlFile));

        List<BibMarcRecord> records = bibliographicRecordCollection.getRecords();
        for (Iterator<BibMarcRecord> iterator = records.iterator(); iterator.hasNext(); ) {
            BibMarcRecord bibMarcRecord = iterator.next();
            OleItemRecordHandler oleItemRecordHandler = new OleItemRecordHandler();
            String itemXML = oleItemRecordHandler.generateXML(bibMarcRecord, Collections.<ProfileAttribute>emptyList());
            LOG.info(itemXML);
        }
        FileUtils.deleteQuietly(xmlFile);
    }
}
