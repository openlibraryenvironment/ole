package org.kuali.ole.docstore.common.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 1/8/14
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class BibMarc_UT {

    private static final Logger LOG = Logger.getLogger(BibMarc_UT.class);

    @Test
    public void deserializeAndSerialize(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        BibMarc bibMarc = new BibMarc();
        bibMarc = (BibMarc) bibMarc.deserialize(input);
        bibMarc.setAuthor("author");
        bibMarc.setTitle("title");
        String serializeXml = bibMarc.serialize(bibMarc);
        System.out.print(serializeXml);
    }

    @Test
    public void deserializeContentAndSerializeContent(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibMarc1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        BibMarc bibMarc = new BibMarc();
        bibMarc = (BibMarc) bibMarc.deserialize(input);
        BibMarcRecords bibMarcRecords = (BibMarcRecords) bibMarc.deserializeContent(bibMarc.getContent());
        String serializeXml = bibMarc.serializeContent(bibMarcRecords);
        System.out.println(serializeXml);
    }
}
