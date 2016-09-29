package org.kuali.ole.gobi.request;

import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.ObjectFactory;
import org.kuali.ole.gobi.datobjects.PurchaseOrder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class GobiRequestHandler {
    public GobiRequest unmarshal(String requestXML) {
        GobiRequest gobiRequest = new GobiRequest();

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            StringReader stringReader = new StringReader(requestXML);

            PurchaseOrder purchaseOrder = (PurchaseOrder) jaxbUnmarshaller.unmarshal(stringReader);
            gobiRequest.setPurchaseOrder(purchaseOrder);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return gobiRequest;

    }
}
