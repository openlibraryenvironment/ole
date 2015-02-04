package org.kuali.ole.docstore.discovery.circulation;

import org.codehaus.jackson.map.ObjectMapper;
import groovy.json.JsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/22/13
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class XmlContentHandler {
    private XMLInputFactory xmlInputFactory;
    private static final Logger LOG             = LoggerFactory.getLogger(XmlContentHandler.class);
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
        }
        if (unmarshaledObject instanceof JAXBElement) {
            return ((JAXBElement) unmarshaledObject).getValue();
        }
        return unmarshaledObject;
    }

    public String marshalToJSON(Object object) throws JAXBException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.defaultPrettyPrintingWriter().writeValueAsString(object);
    }

    private XMLInputFactory getXmlInputFactory() {
        if (null == xmlInputFactory) {
            xmlInputFactory = XMLInputFactory.newInstance();
        }
        return xmlInputFactory;
    }
}
