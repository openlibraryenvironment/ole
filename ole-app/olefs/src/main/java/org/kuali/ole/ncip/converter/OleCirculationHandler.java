package org.kuali.ole.ncip.converter;

import groovy.json.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 9/2/13
 * Time: 4:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCirculationHandler {
    private XMLInputFactory xmlInputFactory;
    private static final Logger LOG             = LoggerFactory.getLogger(OleCirculationHandler.class);
    public Object unmarshalXMLContent(Class clazz, String content) {
        Object unmarshaledObject = null;
        try {
            JAXBContext jaxbContext = JAXBContextFactory.getJAXBContextForClass(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory xmlInputFactory = getXmlInputFactory();
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(new StringReader(content));
            unmarshaledObject = jaxbUnmarshaller.unmarshal(xmlStreamReader);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if (unmarshaledObject instanceof JAXBElement) {
            return ((JAXBElement) unmarshaledObject).getValue();
        }
        return unmarshaledObject;
    }

    public String marshalToJSON(Object object) throws JAXBException {
        JsonBuilder jsonBuilder = new JsonBuilder(object);
        return jsonBuilder.toPrettyString();
    }

    private XMLInputFactory getXmlInputFactory() {
        if (null == xmlInputFactory) {
            xmlInputFactory = XMLInputFactory.newInstance();
        }
        return xmlInputFactory;
    }
}
