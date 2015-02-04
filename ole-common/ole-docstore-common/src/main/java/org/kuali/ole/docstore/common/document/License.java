package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.util.ParseXml;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "license")
public class License extends DocstoreDocument {

    private static final Logger LOG = Logger.getLogger(License.class);

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        License license = (License) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(License.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(license, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        JAXBElement<License> licenseElement = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(content)));
            NodeList root = doc.getChildNodes();
            Node license = ParseXml.getNode("license", root);
                NodeList nodes = license.getChildNodes();
                String licensexml = ParseXml.nodeToString(license);
                if (ParseXml.getNodeValue("format", nodes).equals("onixpl")) {
                    JAXBContext jc = JAXBContext.newInstance(LicenseOnixpl.class);
                    Unmarshaller unmarshaller1 = jc.createUnmarshaller();
                    StreamSource xmlSource = new StreamSource(new StringReader(licensexml));
                    JAXBElement<LicenseOnixpl> je1 = unmarshaller1.unmarshal(xmlSource, LicenseOnixpl.class);
                    LicenseOnixpl licenseOnixpl = je1.getValue();
                    return licenseOnixpl;
                } else {
                    JAXBContext jc = JAXBContext.newInstance(LicenseAttachment.class);
                    Unmarshaller unmarshaller1 = jc.createUnmarshaller();
                    StreamSource xmlSource = new StreamSource(new StringReader(licensexml));
                    JAXBElement<LicenseAttachment> je1 = unmarshaller1.unmarshal(xmlSource, LicenseAttachment.class);
                    LicenseAttachment licenseAttachment = je1.getValue();
                    return licenseAttachment;
                }
        } catch (ParserConfigurationException e) {
            LOG.error("Exception ", e);
        } catch (SAXException e) {
            LOG.error("Exception ", e);
        } catch (IOException e) {
            LOG.error("Exception ", e);
        } catch (JAXBException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    @Override
    public Object deserializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Object deserializeContent(String content) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String serializeContent(Object object) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
