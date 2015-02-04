package org.kuali.ole.docstore.model.xmlpojo.config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kuali.ole.docstore.model.jaxb.config.DocumentConfigConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>
 * Java class for DocumentConfig complex type.
 * <p/>
 * <p/>
 * The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="DocumentConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentCategories" type="{}DocumentCategory" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "documentConfig")
@XmlType(name = "DocumentConfig")
public class DocumentConfig {

    public static Logger logger = LoggerFactory.getLogger(DocumentConfig.class);
    public static DocumentConfig docStoreMetaData = null;
    @XmlElement(name = "documentCategory")
    protected List<DocumentCategory> documentCategories;

    /**
     * Gets the value of the documentCategories property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the documentCategories property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <p/>
     * <pre>
     * getDocumentCategories().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link DocumentCategory }
     */
    public List<DocumentCategory> getDocumentCategories() {
        if (documentCategories == null) {
            documentCategories = new ArrayList<DocumentCategory>();
        }
        return this.documentCategories;
    }

    public static DocumentConfig getInstance() {
        if (docStoreMetaData == null) {
            try {
                DocumentConfigConverter documentConverter = new DocumentConfigConverter();
                String docConfigFilePath = System.getProperty("document.config.file");
                if (docConfigFilePath != null) {
                    logger.info("docConfigFilePath-->" + docConfigFilePath);
                    File docConfigFile = new File(docConfigFilePath);
                    docStoreMetaData = documentConverter.unmarshal(FileUtils.readFileToString(docConfigFile));
                } else {
                    docStoreMetaData = documentConverter.unmarshal(IOUtils.toString(DocumentConfig.class
                            .getResourceAsStream("/org/kuali/ole/docstore/DocumentConfig.xml")));
                }
                logger.info("docStoreMetaData-->" + docStoreMetaData);
                logger.info("Loaded Doc Store Meta Data Successfully.");
            } catch (Exception e) {
                logger.error("Failed in Loading Doc Store Meta Data : Cause : " + e.getMessage(), e);
            }
        }
        return docStoreMetaData;
    }

}
