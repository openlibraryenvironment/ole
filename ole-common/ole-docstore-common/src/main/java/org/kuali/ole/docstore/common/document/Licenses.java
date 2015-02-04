package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.util.ParseXml;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "licenses", propOrder = {
        "licenses"
})
@XmlRootElement
public class Licenses {

    private static final Logger LOG = Logger.getLogger(Licenses.class);

    @XmlElement(name = "license")
    protected List<License> licenses;

    public List<License> getLicenses() {
        if (licenses == null) {
            licenses = new ArrayList<>();
        }
        return licenses;
    }

    public static String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        Licenses licenses = (Licenses) object;
        for(License license : licenses.getLicenses()) {
            if(license instanceof LicenseOnixpl) {
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(LicenseOnixpl.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    jaxbMarshaller.marshal(license, sw);
                } catch (Exception e) {
                    LOG.error("Exception :", e);
                }

            }
            else {
                try {
                    JAXBContext jaxbContext = JAXBContext.newInstance(LicenseAttachment.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                    jaxbMarshaller.marshal(license, sw);
                } catch (Exception e) {
                    LOG.error("Exception :", e);
                }

            }
        }

        result = "<licenses>" + sw.append("</licenses>").toString();
        result = result.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
        return result;
    }

    public static Object deserialize(String licensesXml) {
        JAXBElement<Licenses> licensesElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Licenses.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(licensesXml.getBytes("UTF-8"));
            licensesElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), Licenses.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        Licenses licenses = licensesElement.getValue();
        licenses.getLicenses().clear();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(licensesXml)));
            NodeList root = doc.getChildNodes();
            Node licenseTree = ParseXml.getNode("licenses", root);
            NodeList nodeList = doc.getElementsByTagName("license");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node license = ParseXml.getNodeXml(licenseTree.getChildNodes(), i);
                NodeList nodes = license.getChildNodes();
                String licensexml = ParseXml.nodeToString(license);
                if (ParseXml.getNodeValue("format", nodes).equals("onixpl")) {
                    JAXBContext jc = JAXBContext.newInstance(LicenseOnixpl.class);
                    Unmarshaller unmarshaller1 = jc.createUnmarshaller();
                    StreamSource xmlSource = new StreamSource(new StringReader(licensexml));
                    JAXBElement<LicenseOnixpl> je1 = unmarshaller1.unmarshal(xmlSource, LicenseOnixpl.class);
                    LicenseOnixpl licenseOnixpl = je1.getValue();
                    licenses.getLicenses().add(licenseOnixpl);
                } else {
                    JAXBContext jc = JAXBContext.newInstance(LicenseAttachment.class);
                    Unmarshaller unmarshaller1 = jc.createUnmarshaller();
                    StreamSource xmlSource = new StreamSource(new StringReader(licensexml));
                    JAXBElement<LicenseAttachment> je1 = unmarshaller1.unmarshal(xmlSource, LicenseAttachment.class);
                    LicenseAttachment licenseAttachment = je1.getValue();
                    licenses.getLicenses().add(licenseAttachment);
                }
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
        return licenses;
    }

}
