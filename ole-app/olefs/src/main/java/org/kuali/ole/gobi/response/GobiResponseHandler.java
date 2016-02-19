package org.kuali.ole.gobi.response;

import org.kuali.ole.gobi.GobiRequest;
import org.kuali.ole.gobi.datobjects.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by pvsubrah on 9/14/15.
 */
public class GobiResponseHandler {
    public String marshall(Response response) {
        StringWriter writer = null;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(org.kuali.ole.gobi.response.ObjectFactory.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
            writer = new StringWriter();
            marshaller.marshal(response, writer);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return writer.toString();

    }
}
