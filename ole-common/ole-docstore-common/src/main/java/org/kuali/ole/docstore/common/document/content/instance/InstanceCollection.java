
package org.kuali.ole.docstore.common.document.content.instance;

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
 * <p>Java class for instanceCollection complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="instanceCollection">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="instance" type="{http://ole.kuali.org/standards/ole-instance}instance" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instanceCollection", namespace = "http://ole.kuali.org/standards/ole-instance", propOrder = {
        "instance"
})
@XStreamAlias("instanceCollection")
@XmlRootElement(name = "instanceCollection", namespace = "http://ole.kuali.org/standards/ole-instance")
public class InstanceCollection {

    @XmlElement(name = "instance", namespace = "http://ole.kuali.org/standards/ole-instance", required = true)
    @XStreamImplicit(itemFieldName = "instance")
    protected List<Instance> instance;

    /**
     * Gets the value of the instance property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the instance property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInstance().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link Instance }
     */
    public List<Instance> getInstance() {
        if (instance == null) {
            instance = new ArrayList<Instance>();
        }
        return this.instance;
    }

    public void setInstance(List<Instance> instance) {
        this.instance = instance;
    }
}
