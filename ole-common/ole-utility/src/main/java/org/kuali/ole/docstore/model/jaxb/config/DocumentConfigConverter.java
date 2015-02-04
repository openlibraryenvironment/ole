package org.kuali.ole.docstore.model.jaxb.config;

import org.kuali.ole.docstore.model.xmlpojo.config.DocumentConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 6/22/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentConfigConverter {
    public static final Logger LOG = LoggerFactory.getLogger(DocumentConfigConverter.class);

    public DocumentConfig unmarshal(String docXml) {

        JAXBElement<DocumentConfig> docElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DocumentConfig.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(docXml.getBytes("UTF-8"));
            docElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), DocumentConfig.class);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return docElement.getValue();
    }

    public String marshal(DocumentConfig docConfig) {

        JAXBElement<DocumentConfig> docElement = null;
        String result = null;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DocumentConfig.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(docConfig, sw);
            result = sw.toString();
            LOG.info("DocSearchConfig Xml is " + result);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return result;
    }
}
