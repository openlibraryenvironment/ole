package org.kuali.ole.docstore.common.document;

import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.exception.DocstoreDeserializeException;
import org.kuali.ole.docstore.common.exception.DocstoreResources;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;


/**
 * <p>Java class for bib complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="bib">
 *   &lt;complexContent>
 *     &lt;extension base="{}docstoreDocument">
 *       &lt;sequence>
 *         &lt;element name="issn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isbn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="edition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicationDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publisher" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="author" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bibDoc", propOrder = {
        "issn",
        "isbn",
        "subject",
        "edition",
        "publicationDate",
        "publisher",
        "author",
        "title"
})
@XmlRootElement(name = "bibDoc")
public class Bib
        extends DocstoreDocument {

    private static final Logger LOG = Logger.getLogger(Bib.class);
    public static final String TITLE = "TITLE";
    public static final String AUTHOR = "AUTHOR";
    public static final String ISBN = "ISBN";
    public static final String ISSN = "ISSN";
    public static final String SUBJECT = "SUBJECT";
    public static final String PUBLISHER = "PUBLISHER";
    public static final String PUBLICATIONDATE = "PUBLICATIONDATE";
    public static final String EDITION = "EDITION";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String FORMAT = "FORMAT";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String BIBIDENTIFIER = "BIBIDENTIFIER";
    protected String issn;
    protected String isbn;
    protected String subject;
    protected String edition;
    protected String publicationDate;
    protected String publisher;
    protected String author;
    protected String title;

    public Bib() {
        category=DocCategory.WORK.getCode();
        type=DocType.BIB.getCode();
        format=DocFormat.MARC.getCode();
    }

    /**
     * Gets the value of the issn property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIssn() {
        return issn;
    }

    /**
     * Sets the value of the issn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIssn(String value) {
        this.issn = value;
    }

    /**
     * Gets the value of the isbn property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the value of the isbn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIsbn(String value) {
        this.isbn = value;
    }

    /**
     * Gets the value of the subject property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets the value of the edition property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getEdition() {
        return edition;
    }

    /**
     * Sets the value of the edition property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEdition(String value) {
        this.edition = value;
    }

    /**
     * Gets the value of the publicationDate property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPublicationDate() {
        return publicationDate;
    }

    /**
     * Sets the value of the publicationDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublicationDate(String value) {
        this.publicationDate = value;
    }

    /**
     * Gets the value of the publisher property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the value of the publisher property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublisher(String value) {
        this.publisher = value;
    }

    /**
     * Gets the value of the author property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

    @Override
    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        Bib bib = (Bib) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Bib.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            jaxbMarshaller.marshal(bib, sw);
            result = sw.toString();
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }

    @Override
    public Object deserialize(String content) {

        JAXBElement<Bib> bibElement = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Bib.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes("UTF-8"));
            bibElement = jaxbUnmarshaller.unmarshal(new StreamSource(input), Bib.class);
        } catch (Exception e) {
            LOG.error("Exception :", e);
            throw new DocstoreDeserializeException(DocstoreResources.BIB_CREATION_FAILED,DocstoreResources.BIB_CREATION_FAILED);
        }
        return bibElement.getValue();
    }

    @Override
    public Object deserializeContent(Object object) {
        Bib bib = (Bib) object;
        BibMarcMapping bibMarcMapping = new BibMarcMapping();
        return bibMarcMapping.getDocument(bib);
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
