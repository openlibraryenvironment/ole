package org.kuali.ole.docstore.common.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.common.util.ParseXml;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 1/8/14
 * Time: 11:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemOleml_UT {

    private static final Logger LOG = Logger.getLogger(ItemOleml_UT.class);
    @Test
    public void deserializeAndSerialize(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/ItemOleml1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        ItemOleml item = new ItemOleml();
        item = (ItemOleml) item.deserialize(input);
        item.setCallNumber("123");
        item.setCallNumberType("LCC");
        item.setBarcode("123456");
        String serializeXml = item.serialize(item);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.print(serializeXml);

    }

    @Test
    public void deserializeContentAndSerializeContent(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/ItemOleml1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        ItemOleml item = new ItemOleml();
        item = (ItemOleml) item.deserialize(input);
        org.kuali.ole.docstore.common.document.content.instance.Item itemDocument = (org.kuali.ole.docstore.common.document.content.instance.Item) item.deserializeContent(item.getContent());
        String serializeXml = item.serializeContent(itemDocument);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.print(serializeXml);
    }

}
