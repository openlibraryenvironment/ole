package org.kuali.ole;

import org.junit.Test;
import org.kuali.ole.converter.OLEEDIConverter;
import org.kuali.ole.converter.OLEINVConverter;
import org.kuali.ole.docstore.xstream.FileUtil;
import org.kuali.ole.pojo.edi.EDIOrder;
import org.kuali.ole.pojo.edi.EDIOrders;
import org.kuali.ole.pojo.edi.INVOrder;
import org.kuali.ole.pojo.edi.INVOrders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/8/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleInvoiceRecordBuilder_UT {

    protected static final Logger LOG = LoggerFactory.getLogger(OleInvoiceRecordBuilder_UT.class);

    @Test
    public void testBuild() throws Exception {
        OLEINVConverter OLEEDITranslator = new OLEINVConverter();
        assertNotNull(OLEEDITranslator);
        URL ediResource = getClass().getResource("R032713.T102543.INV");
        File ediFile = new File(ediResource.toURI());
        String fileContent = new FileUtil().readFile(ediFile);
        String javaResult = OLEEDITranslator.convertToXML(fileContent);

        System.out.println("Invoice XML-----------------" + javaResult);


    }


    /**
     * This method is used to generate POJOS from XML.
     *
     * @throws Exception
     */
    @Test
    public void testGenerateXMLToPojos() throws Exception {
        OLEINVConverter OLEEDITranslator = new OLEINVConverter();
        assertNotNull(OLEEDITranslator);
        URL ediResource = getClass().getResource("R032713.T102543.INV");
        File ediFile = new File(ediResource.toURI());
        String fileContent = new FileUtil().readFile(ediFile);
        String xml = OLEEDITranslator.convertToXML(fileContent);

        OLETranscationalRecordGenerator oleTranscationalRecordGenerator = new OLETranscationalRecordGenerator();
        INVOrders invOrders = oleTranscationalRecordGenerator.fromInvoiceXml(xml);

        for (Iterator<INVOrder> iterator = invOrders.getInvOrder().iterator(); iterator.hasNext(); ) {
            INVOrder invOrder = iterator.next();
            LOG.info("==============Object Message Out=============");
            LOG.info(invOrders.getSenderAndReceiver().getSendersAndReceiversConstant().getCode());
            LOG.info(invOrders.getSenderAndReceiver().getSenderInformation().getSenderId());
            LOG.info(invOrders.getSenderAndReceiver().getReceiverInformation().getReceiverId());
            LOG.info(invOrders.getSenderAndReceiver().getSchedule().getPreparationDate());
            LOG.info(invOrders.getSenderAndReceiver().getInterChangeControlReference());
            LOG.info("======================================\n\n");
        }
    }
}
