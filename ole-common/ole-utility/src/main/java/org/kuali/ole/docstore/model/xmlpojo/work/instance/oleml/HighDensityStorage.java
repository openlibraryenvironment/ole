
package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;

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
 * Does not map to MFHD. Identifies the Row, Module, Shelf, and Tray information for
 * the
 * item's High Density Storage location.
 * <p/>
 * <p/>
 * <p>Java class for highDensityStorage complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="highDensityStorage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="row" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="module" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="shelf" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tray" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "highDensityStorage", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
        "row",
        "module",
        "shelf",
        "tray"
})
@XStreamAlias("highDensityStorage")
public class HighDensityStorage {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String row;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String module;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String shelf;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String tray;

    /**
     * Gets the value of the row property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getRow() {
        return row;
    }

    /**
     * Sets the value of the row property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRow(String value) {
        this.row = value;
    }

    /**
     * Gets the value of the module property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getModule() {
        return module;
    }

    /**
     * Sets the value of the module property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setModule(String value) {
        this.module = value;
    }

    /**
     * Gets the value of the shelf property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getShelf() {
        return shelf;
    }

    /**
     * Sets the value of the shelf property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setShelf(String value) {
        this.shelf = value;
    }

    /**
     * Gets the value of the tray property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getTray() {
        return tray;
    }

    /**
     * Sets the value of the tray property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTray(String value) {
        this.tray = value;
    }

}
