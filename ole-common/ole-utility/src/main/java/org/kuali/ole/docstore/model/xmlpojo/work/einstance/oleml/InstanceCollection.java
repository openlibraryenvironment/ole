package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for instanceCollection complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="instanceCollection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="eInstance" type="{http://ole.kuali.org/standards/ole-eInstance}eInstance" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instanceCollection", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "eInstance"
})
@XStreamAlias("instanceCollection")
@XmlRootElement(name = "instanceCollection", namespace = "http://ole.kuali.org/standards/ole-eInstance")
public class InstanceCollection {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance", required = true)
    @XStreamImplicit(itemFieldName = "eInstance")
    protected List<EInstance> eInstance;

    /**
     * Gets the value of the eInstance property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the eInstance property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEInstance().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.EInstance }
     */
    public List<EInstance> getEInstance() {
        if (eInstance == null) {
            eInstance = new ArrayList<EInstance>();
        }
        return this.eInstance;
    }

}
