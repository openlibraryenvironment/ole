package org.kuali.ole.deliver;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.deliver.bo.OlePatronLoanDocuments;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/25/12
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */


public class OlePatronLoanDocumentJaxb_UT {

    private static final Logger LOG = Logger.getLogger(OlePatronLoanDocumentJaxb_UT.class);

    @Test
    public void testMarshalAndUnmarshall() throws Exception {
        LOG.debug("Inside the testMarshalAndUnmarshall method");
        JAXBContext jaxb = JAXBContext.newInstance(OlePatronLoanDocuments.class);
        OlePatronLoanDocuments patronLoanDocuments = OlePatronLoanDocumentHelper.create();

        Marshaller marshaller = jaxb.createMarshaller();
        StringWriter writer = new StringWriter();
        marshaller.marshal(patronLoanDocuments, writer);
        LOG.info("Marshalled Patron is: " + writer.toString());
        Unmarshaller unmarshaller = jaxb.createUnmarshaller();
        OlePatronLoanDocuments unmarshalled = (OlePatronLoanDocuments)unmarshaller.unmarshal(new StringReader(writer.toString()));
        assertEquals(patronLoanDocuments.getOlePatronLoanDocuments().get(0).getItemBarcode(), unmarshalled.getOlePatronLoanDocuments().get(0).getItemBarcode());
        assertEquals(patronLoanDocuments.getOlePatronLoanDocuments().get(0).getTitle(), unmarshalled.getOlePatronLoanDocuments().get(0).getTitle());
        assertEquals(patronLoanDocuments.getOlePatronLoanDocuments().get(0).getAuthor(), unmarshalled.getOlePatronLoanDocuments().get(0).getAuthor());
        assertEquals(patronLoanDocuments.getOlePatronLoanDocuments().get(0).getCallNumber(), unmarshalled.getOlePatronLoanDocuments().get(0).getCallNumber());
        assertEquals(patronLoanDocuments.getOlePatronLoanDocuments().get(0).getLocation(), unmarshalled.getOlePatronLoanDocuments().get(0).getLocation());


    }
}
