package org.kuali.ole.docstore.common.document.content.license.onixpl.jaxb;

import org.kuali.ole.docstore.common.document.content.license.onixpl.ONIXPublicationsLicenseMessage;
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
 * Date: 5/30/12
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class LicenseOnixplRecordConverter {
    public static final Logger LOG = LoggerFactory.getLogger(LicenseOnixplRecordConverter.class);

    public ONIXPublicationsLicenseMessage unmarshal(String onixXml) {

        JAXBElement<ONIXPublicationsLicenseMessage> onixElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ONIXPublicationsLicenseMessage.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(onixXml.getBytes("UTF-8"));
            onixElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), ONIXPublicationsLicenseMessage.class);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return onixElement.getValue();
    }

    public String marshal(ONIXPublicationsLicenseMessage onixpl) {

        String result = null;
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ONIXPublicationsLicenseMessage.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(onixpl, sw);
            result = sw.toString();
            LOG.info("DocSearchConfig Xml is " + result);
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        return result;
    }
}




