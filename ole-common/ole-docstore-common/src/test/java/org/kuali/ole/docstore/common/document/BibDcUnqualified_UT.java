package org.kuali.ole.docstore.common.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.bib.dc.unqualified.BibDublinUnQualifiedRecord;
import org.kuali.ole.docstore.common.util.ParseXml;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: sambasivam
 * Date: 1/21/14
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class BibDcUnqualified_UT {

    private static final Logger LOG = Logger.getLogger(BibDcUnqualified_UT.class);

    @Test
    public void deserializeAndSerialize(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/BibDUnqualified1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        BibDcUnqualified bibDcUnqualified = new BibDcUnqualified();
        bibDcUnqualified = (BibDcUnqualified) bibDcUnqualified.deserialize(input);
        bibDcUnqualified.setAuthor("author");
        bibDcUnqualified.setTitle("title");
        String serializeXml = bibDcUnqualified.serialize(bibDcUnqualified);
        serializeXml = ParseXml.formatXml(serializeXml);
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
        BibDcUnqualified bibDcUnqualified = new BibDcUnqualified();
        bibDcUnqualified = (BibDcUnqualified) bibDcUnqualified.deserialize(input);
        BibDublinUnQualifiedRecord bibMarcRecords = (BibDublinUnQualifiedRecord) bibDcUnqualified.deserializeContent(bibDcUnqualified.getContent());
        String serializeXml = bibDcUnqualified.serializeContent(bibMarcRecords);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.println(serializeXml);
    }
}
