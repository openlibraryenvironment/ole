package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * The eInstance captures holdings information. Holdings can be recorded in either the general
 * holdings section or specific holdings format in the sourceHoldings section.
 * <p/>
 * <p/>
 * <p>Java class for eInstance complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="eInstance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="instanceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="resourceIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="formerResourceIdentifier" type="{http://ole.kuali.org/standards/ole-eInstance}formerIdentifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="eHoldings" type="{http://ole.kuali.org/standards/ole-eInstance}eHoldings"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eInstance", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "instanceIdentifier",
        "resourceIdentifier",
        "formerResourceIdentifier",
        "eHoldings"
})
@XStreamAlias("eInstance")
@XmlRootElement(name = "instance", namespace = "http://ole.kuali.org/standards/ole-eInstance")
public class EInstance {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected String instanceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    @XStreamImplicit(itemFieldName = "resourceIdentifier")
    protected List<String> resourceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    @XStreamImplicit(itemFieldName = "formerResourceIdentifier")
    protected List<FormerIdentifier> formerResourceIdentifier;
    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    protected EHoldings eHoldings;

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
     * {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.FormerIdentifier }
     */
    public List<FormerIdentifier> getFormerResourceIdentifier() {
        if (formerResourceIdentifier == null) {
            formerResourceIdentifier = new ArrayList<FormerIdentifier>();
        }
        return this.formerResourceIdentifier;
    }

    /**
     * Gets the value of the eHoldings property.
     *
     * @return possible object is
     *         {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EHoldings }
     */
    public EHoldings getEHoldings() {
        return eHoldings;
    }

    /**
     * Sets the value of the eHoldings property.
     *
     * @param value allowed object is
     *              {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EHoldings }
     */
    public void setEHoldings(EHoldings value) {
        this.eHoldings = value;
    }

}
