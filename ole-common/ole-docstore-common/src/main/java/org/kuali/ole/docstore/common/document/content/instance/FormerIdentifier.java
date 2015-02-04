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
 * <p>Java class for formerIdentifier complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="formerIdentifier">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://ole.kuali.org/standards/ole-instance}identifier"/>
 *         &lt;element name="identifierType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formerIdentifier", propOrder = {
        "identifier",
        "identifierType"
})
@XStreamAlias("formerIdentifier")
public class FormerIdentifier {

    @XmlElement(required = true)
    protected Identifier identifier;
    @XmlElement(required = true)
    protected String identifierType;

    /**
     * Gets the value of the identifier property.
     *
     * @return possible object is
     *         {@link Identifier }
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     *
     * @param value allowed object is
     *              {@link Identifier }
     */
    public void setIdentifier(Identifier value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the identifierType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getIdentifierType() {
        return identifierType;
    }

    /**
     * Sets the value of the identifierType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIdentifierType(String value) {
        this.identifierType = value;
    }

}
