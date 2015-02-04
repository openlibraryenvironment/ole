
package org.kuali.ole.docstore.model.xmlpojo.work.instance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * The instance captures holdings and item information. Holdings can be recorded in either the general
 * holdings section or specific holdings format in the sourceHoldings section. The flag "primary" on
 * either
 * holdings or the sourceHoldings tag indicates which holdings is being used. OLE will support MFHD and
 * any
 * other specific holdings format will need to be accounted for and implemented by the individual
 * institutions.
 * At any given time, either the general holdings or the sourceHoldings can be in use.
 * <p/>
 * The item information is also recorded and also maps to fields in the MFHD record. OLE will provide
 * the
 * conversion if an institution is using MFHD, bearing in mind that in such an event the location will
 * only be limited to 3 levels.
 * <p/>
 * <p/>
 * <p>Java class for instance complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="instance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="instanceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resourceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *         &lt;element name="formerResourceIdentifier" type="{http://ole.kuali.org/standards/ole-instance}formerIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="oleHoldings" type="{http://ole.kuali.org/standards/ole-instance}oleHoldings"/>
 *         &lt;element name="sourceHoldings" type="{http://ole.kuali.org/standards/ole-instance}sourceHoldings"/>
 *         &lt;element name="items" type="{http://ole.kuali.org/standards/ole-instance}items"/>
 *         &lt;element name="extension" type="{http://ole.kuali.org/standards/ole-instance}extension"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instance", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
        "instanceIdentifier",
        "resourceIdentifier",
        "formerResourceIdentifier",
        "oleHoldings",
        "sourceHoldings",
        "items",
        "extension"
})
@XStreamAlias("instance")
@XmlRootElement(name = "instance", namespace = "http://ole.kuali.org/standards/ole-instance")
public class Instance {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected String instanceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    @XStreamImplicit(itemFieldName = "resourceIdentifier")
    protected List<String> resourceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance")
    @XStreamImplicit(itemFieldName = "formerResourceIdentifier")
    protected List<FormerIdentifier> formerResourceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected OleHoldings oleHoldings;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected SourceHoldings sourceHoldings;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Items items;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    protected Extension extension;

    /**
     * Gets the value of the instanceIdentifier property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getInstanceIdentifier() {
        return instanceIdentifier;
    }

    /**
     * Sets the value of the instanceIdentifier property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInstanceIdentifier(String value) {
        this.instanceIdentifier = value;
    }

    /**
     * Gets the value of the resourceIdentifier property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resourceIdentifier property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResourceIdentifier().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getResourceIdentifier() {
        if (resourceIdentifier == null) {
            resourceIdentifier = new ArrayList<String>();
        }
        return this.resourceIdentifier;
    }

    /**
     * @param resourceIdentifier
     */
    public void setResourceIdentifier(List<String> resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    /**
     * Gets the value of the formerResourceIdentifier property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the formerResourceIdentifier property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFormerResourceIdentifier().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link FormerIdentifier }
     */
    public List<FormerIdentifier> getFormerResourceIdentifier() {
        if (formerResourceIdentifier == null) {
            formerResourceIdentifier = new ArrayList<FormerIdentifier>();
        }
        return this.formerResourceIdentifier;
    }

    /**
     * @param formerResourceIdentifier
     */
    public void setFormerResourceIdentifier(List<FormerIdentifier> formerResourceIdentifier) {
        this.formerResourceIdentifier = formerResourceIdentifier;
    }

    /**
     * Gets the value of the oleHoldings property.
     *
     * @return possible object is
     *         {@link OleHoldings }
     */
    public OleHoldings getOleHoldings() {
        return oleHoldings;
    }

    /**
     * Sets the value of the oleHoldings property.
     *
     * @param value allowed object is
     *              {@link OleHoldings }
     */
    public void setOleHoldings(OleHoldings value) {
        this.oleHoldings = value;
    }

    /**
     * Gets the value of the sourceHoldings property.
     *
     * @return possible object is
     *         {@link SourceHoldings }
     */
    public SourceHoldings getSourceHoldings() {
        return sourceHoldings;
    }

    /**
     * Sets the value of the sourceHoldings property.
     *
     * @param value allowed object is
     *              {@link SourceHoldings }
     */
    public void setSourceHoldings(SourceHoldings value) {
        this.sourceHoldings = value;
    }

    /**
     * Gets the value of the items property.
     *
     * @return possible object is
     *         {@link Items }
     */
    public Items getItems() {
        if (items == null) {
            items = new Items();
        }
        return items;
    }

    /**
     * Sets the value of the items property.
     *
     * @param value allowed object is
     *              {@link Items }
     */
    public void setItems(Items value) {
        this.items = value;
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

}
