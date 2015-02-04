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
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class PHoldingsOleml_UT {

    private static final Logger LOG = Logger.getLogger(PHoldingsOleml_UT.class);
    @Test
    public void deserializeAndSerialize(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/PHoldingsOleml1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        PHoldingsOleml pHoldingsOleml = new PHoldingsOleml();
        pHoldingsOleml = (PHoldingsOleml) pHoldingsOleml.deserialize(input);
        pHoldingsOleml.setCallNumber("123");
        pHoldingsOleml.setCallNumberType("LCC");
        String serializeXml = pHoldingsOleml.serialize(pHoldingsOleml);
        serializeXml= ParseXml.formatXml(serializeXml);
        System.out.print(serializeXml);
    }

    @Test
    public void deserializeContentAndSerializeContent(){
        String input ="";
        File file = null;
        try {
            file = new File(getClass().getResource("/documents/PHoldingsOleml1.xml").toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            LOG.error("Exception ", e);
        }
        PHoldingsOleml pHoldingsOleml = new PHoldingsOleml();
        pHoldingsOleml = (PHoldingsOleml) pHoldingsOleml.deserialize(input);
        OleHoldings oleHoldings = (OleHoldings) pHoldingsOleml.deserializeContent(pHoldingsOleml.getContent());
        String serializeXml = pHoldingsOleml.serializeContent(oleHoldings);
        serializeXml = ParseXml.formatXml(serializeXml);
        System.out.print(serializeXml);
    }

}
