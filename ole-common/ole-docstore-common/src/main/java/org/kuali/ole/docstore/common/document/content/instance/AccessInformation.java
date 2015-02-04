package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for accessInformation complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="accessInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="barcode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="uri" type="{http://ole.kuali.org/standards/ole-instance}uri"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accessInformation", propOrder = {
        "barcode",
        "uri"
})
@XStreamAlias("accessInformation")
public class AccessInformation {

    @XmlElement(required = true)
    protected String barcode;
    @XmlElement(required = true)
    protected Uri uri;

    /**
     * Gets the value of the barcode property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * Sets the value of the barcode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBarcode(String value) {
        this.barcode = value;
    }

    /**
     * Gets the value of the uri property.
     *
     * @return possible object is
     *         {@link Uri }
     */
    public Uri getUri() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     *
     * @param value allowed object is
     *              {@link Uri }
     */
    public void setUri(Uri value) {
        this.uri = value;
    }

}
