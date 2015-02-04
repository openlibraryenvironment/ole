
package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.xml.bind.annotation.*;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for sourceHoldings complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="sourceHoldings">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="holdingsIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="holdings" type="{http://ole.kuali.org/standards/ole-instance}extension"/>
 *         &lt;element name="extension" type="{http://ole.kuali.org/standards/ole-instance}extension"/>
 *       &lt;/sequence>
 *       &lt;attribute name="primary" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sourceHoldings", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
        "holdingsIdentifier",
        "name",
        "holdings",
        "extension"
})
@XStreamAlias("sourceHoldings")
@XmlRootElement(name = "sourceHoldings", namespace = "http://ole.kuali.org/standards/ole-instance")
public class SourceHoldings {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String holdingsIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String name;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Extension holdings;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Extension extension;
    @XmlAttribute
    @XStreamAsAttribute
    protected String primary;

    /**
     * Gets the value of the holdingsIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getHoldingsIdentifier() {
        return holdingsIdentifier;
    }

    /**
     * Sets the value of the holdingsIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setHoldingsIdentifier(String value) {
        this.holdingsIdentifier = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the holdings property.
     *
     * @return possible object is
     *         {@link Extension }
     */
    public Extension getHoldings() {
        return holdings;
    }

    /**
     * Sets the value of the holdings property.
     *
     * @param value allowed object is
     *              {@link Extension }
     */
    public void setHoldings(Extension value) {
        this.holdings = value;
    }

    /**
     * Gets the value of the extension property.
     *
     * @return possible object is
     *         {@link Extension }
     */
    public Extension getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     *
     * @param value allowed object is
     *              {@link Extension }
     */
    public void setExtension(Extension value) {
        this.extension = value;
    }

    /**
     * Gets the value of the primary property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrimary(String value) {
        this.primary = value;
    }

}
