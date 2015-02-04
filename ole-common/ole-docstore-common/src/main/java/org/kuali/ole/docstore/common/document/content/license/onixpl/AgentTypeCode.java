
package org.kuali.ole.docstore.common.document.content.license.onixpl;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 5/30/12
 * Time: 1:18 PM
 * To change this template use File | Settings | File Templates.
 * <p/>
 * <p>Java class for AgentTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="AgentTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Organization"/>
 *     &lt;enumeration value="onixPL:Person"/>
 *     &lt;enumeration value="onixPL:Unspecified"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "AgentTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum AgentTypeCode {


    /**
     * Agent is an organization or group of persons.
     */
    @XmlEnumValue("onixPL:Organization")
    ONIX_PL_ORGANIZATION("onixPL:Organization"),

    /**
     * Agent is a natural person.
     */
    @XmlEnumValue("onixPL:Person")
    ONIX_PL_PERSON("onixPL:Person"),

    /**
     * Agent may be either a natural person or an organization or group.
     */
    @XmlEnumValue("onixPL:Unspecified")
    ONIX_PL_UNSPECIFIED("onixPL:Unspecified");
    private final String value;

    AgentTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AgentTypeCode fromValue(String v) {
        for (AgentTypeCode c : AgentTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
