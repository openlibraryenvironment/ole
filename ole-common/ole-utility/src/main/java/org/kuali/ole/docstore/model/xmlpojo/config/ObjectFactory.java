
package org.kuali.ole.docstore.model.xmlpojo.config;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.kuali.ole.docstore.model.xmlpojo.config package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DocumentConfig_QNAME = new QName("", "documentConfig");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.kuali.ole.docstore.model.xmlpojo.config
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Mapping }
     */
    public Mapping createMapping() {
        return new Mapping();
    }

    /**
     * Create an instance of {@link DocumentType }
     */
    public DocumentType createDocumentType() {
        return new DocumentType();
    }

    /**
     * Create an instance of {@link Field }
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link DocumentCategory }
     */
    public DocumentCategory createDocumentCategory() {
        return new DocumentCategory();
    }

    /**
     * Create an instance of {@link DocumentConfig }
     */
    public DocumentConfig createDocumentConfig() {
        return new DocumentConfig();
    }

    /**
     * Create an instance of {@link DocumentFormat }
     */
    public DocumentFormat createDocumentFormat() {
        return new DocumentFormat();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentConfig }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "documentConfig")
    public JAXBElement<DocumentConfig> createDocumentConfig(DocumentConfig value) {
        return new JAXBElement<DocumentConfig>(_DocumentConfig_QNAME, DocumentConfig.class, null, value);
    }

}
