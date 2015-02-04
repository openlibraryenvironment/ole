package org.kuali.ole.docstore.common.document;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.util.ParseXml;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: srirams
 * Date: 1/8/14
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class EHoldingsOleml_UT {

    private static final Logger LOG = Logger.getLogger(EHoldingsOleml_UT.class);

    @Test
    public void deserializeAndSerialize(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/EHoldingsOleml1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        EHoldings eHoldings = new EHoldings();
        eHoldings = (EHoldings) eHoldings.deserialize(input);
        eHoldings.setCallNumber("123");
        eHoldings.setCallNumberType("LCC");
        String serializeXml = eHoldings.serialize(eHoldings);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.println(serializeXml);

    }

    @Test
    public void deserializeContentAndSerializeContent(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/EHoldingsOleml1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        EHoldingsOleml eHoldings = new EHoldingsOleml();
        eHoldings = (EHoldingsOleml) eHoldings.deserialize(input);
        String content = eHoldings.getContent();
        OleHoldings oleHoldings = (OleHoldings) eHoldings.deserializeContent(content);
        String serializeXml = eHoldings.serializeContent(oleHoldings);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.println(serializeXml);
    }

}
