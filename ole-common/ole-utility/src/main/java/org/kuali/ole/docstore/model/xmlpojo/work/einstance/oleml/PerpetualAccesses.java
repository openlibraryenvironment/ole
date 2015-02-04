package org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for perpetualAccesses complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="perpetualAccesses">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="perpetualAccess" type="{http://ole.kuali.org/standards/ole-eInstance}perpetualAccess" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "perpetualAccesses", namespace = "http://ole.kuali.org/standards/ole-eInstance", propOrder = {
        "perpetualAccess"
})
@XStreamAlias("perpetualAccesses")
public class PerpetualAccesses {

    @XmlElement(namespace = "http://ole.kuali.org/standards/ole-eInstance")
    @XStreamImplicit(itemFieldName = "perpetualAccess")
    protected List<PerpetualAccess> perpetualAccess;

    /**
     * Gets the value of the perpetualAccess property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the perpetualAccess property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerpetualAccess().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link org.kuali.ole.docstore.model.xmlpojo.work.einstance.oleml.PerpetualAccess }
     */
    public List<PerpetualAccess> getPerpetualAccess() {
        if (perpetualAccess == null) {
            perpetualAccess = new ArrayList<PerpetualAccess>();
        }
        return this.perpetualAccess;
    }

}
