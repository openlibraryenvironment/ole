package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
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
 * Time: 2:59 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "license")
public class LicenseAttachment extends License {

    private static final Logger LOG = Logger.getLogger(LicenseAttachment.class);

    private String fileName;
    private String filePath;
    private String documentTitle;
    private String documentMimeType;
    private String documentNote;
    private String agreementType;
    private String agreementNote;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDocumentMimeType() {
        return documentMimeType;
    }

    public void setDocumentMimeType(String documentMimeType) {
        this.documentMimeType = documentMimeType;
    }

    public String getDocumentNote() {
        return documentNote;
    }

    public void setDocumentNote(String documentNote) {
        this.documentNote = documentNote;
    }

    public LicenseAttachment() {
        category = DocCategory.WORK.getCode();
        type = DocType.LICENSE.getCode();
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public String getAgreementNote() {
        return agreementNote;
    }

    public void setAgreementNote(String agreementNote) {
        this.agreementNote = agreementNote;
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        LicenseAttachment licenseAttachment = (LicenseAttachment) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LicenseAttachment.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(licenseAttachment, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {
        JAXBElement<LicenseAttachment> licenseAttachmentElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LicenseAttachment.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            licenseAttachmentElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), LicenseAttachment.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return licenseAttachmentElement.getValue();
    }
}
