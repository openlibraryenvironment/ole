package org.kuali.ole.docstore.common.document.content.instance;

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
 * <p>Java class for callNumber complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="callNumber">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Prefix" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="classificationPart" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="itemPart" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shelvingScheme" type="{http://ole.kuali.org/standards/ole-instance}shelvingScheme"/>
 *         &lt;element name="shelvingOrder" type="{http://ole.kuali.org/standards/ole-instance}shelvingOrder"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "callNumber", propOrder = {
        "type",
        "prefix",
        "number",
        "classificationPart",
        "itemPart",
        "shelvingScheme",
        "shelvingOrder"
})
public class CallNumber {

    protected String type;
    protected String prefix;
    protected String number;
    protected String classificationPart;
    protected String itemPart;
    protected ShelvingScheme shelvingScheme;
    protected ShelvingOrder shelvingOrder;

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the prefix property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the value of the prefix property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrefix(String value) {
        this.prefix = value;
    }

    /**
     * Gets the value of the number property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the classificationPart property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getClassificationPart() {
        return classificationPart;
    }

    /**
     * Sets the value of the classificationPart property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setClassificationPart(String value) {
        this.classificationPart = value;
    }

    /**
     * Gets the value of the itemPart property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getItemPart() {
        return itemPart;
    }

    /**
     * Sets the value of the itemPart property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setItemPart(String value) {
        this.itemPart = value;
    }

    /**
     * Gets the value of the shelvingScheme property.
     *
     * @return possible object is
     *         {@link ShelvingScheme }
     */
    public ShelvingScheme getShelvingScheme() {
        return shelvingScheme;
    }

    /**
     * Sets the value of the shelvingScheme property.
     *
     * @param value allowed object is
     *              {@link ShelvingScheme }
     */
    public void setShelvingScheme(ShelvingScheme value) {
        this.shelvingScheme = value;
    }

    /**
     * Gets the value of the shelvingOrder property.
     *
     * @return possible object is
     *         {@link ShelvingOrder }
     */
    public ShelvingOrder getShelvingOrder() {
        return shelvingOrder;
    }

    /**
     * Sets the value of the shelvingOrder property.
     *
     * @param value allowed object is
     *              {@link ShelvingOrder }
     */
    public void setShelvingOrder(ShelvingOrder value) {
        this.shelvingOrder = value;
    }

}
