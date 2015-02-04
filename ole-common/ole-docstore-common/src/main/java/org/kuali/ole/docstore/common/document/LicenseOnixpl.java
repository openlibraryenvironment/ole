package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: chenchulakshmig
 * Date: 2/25/14
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "license")
public class LicenseOnixpl extends License {

    private static final Logger LOG = Logger.getLogger(LicenseOnixpl.class);

    public LicenseOnixpl() {
        category = DocCategory.WORK.getCode();
        type = DocType.LICENSE.getCode();
        format = DocFormat.ONIXPL.getCode();
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        LicenseOnixpl licenseOnixpl = (LicenseOnixpl) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LicenseOnixpl.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(licenseOnixpl, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        JAXBElement<LicenseOnixpl> licenseOnixplElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LicenseOnixpl.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            licenseOnixplElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), LicenseOnixpl.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return licenseOnixplElement.getValue();
    }
}
