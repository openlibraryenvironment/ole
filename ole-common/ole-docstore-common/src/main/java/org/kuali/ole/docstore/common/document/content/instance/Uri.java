package org.kuali.ole.docstore.common.document.content.instance;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import org.kuali.ole.docstore.common.document.content.instance.xstream.UriConverter;

import javax.xml.bind.annotation.*;


/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 8/17/12
 * Time: 10:47 AM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for uri complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="uri">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="resolvable" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "uri", propOrder = {
        "value"
})
@XStreamAlias("uri")
@XmlRootElement(name = "uri")
@XStreamConverter(value = UriConverter.class)
public class Uri {

    @XmlValue
    protected String value;
    @XmlAttribute
    @XStreamAsAttribute
    protected String resolvable;

    /**
     * Gets the value of the value property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the resolvable property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getResolvable() {
        return resolvable;
    }

    /**
     * Sets the value of the resolvable property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setResolvable(String value) {
        this.resolvable = value;
    }

}
