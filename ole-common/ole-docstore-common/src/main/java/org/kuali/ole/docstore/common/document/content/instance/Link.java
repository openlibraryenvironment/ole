package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for link complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="link">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "link", propOrder = {
        "holdingsUriId",
        "text",
        "url"
})
@XStreamAlias("link")
public class Link {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    protected String holdingsUriId;
    //@XmlElement(name = "text")
    protected String text;
    //@XmlElement(name = "url")
    @XmlSchemaType(name = "anyURI")
    protected String url;

    /**
     * Gets the value of the text property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setText(String value) {
        this.text = value;
    }

    /**
     * Gets the value of the url property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUrl(String value) {
        this.url = value;
    }

    public String getHoldingsUriId() {
        return holdingsUriId;
    }

    public void setHoldingsUriId(String holdingsUriId) {
        this.holdingsUriId = holdingsUriId;
    }
}
