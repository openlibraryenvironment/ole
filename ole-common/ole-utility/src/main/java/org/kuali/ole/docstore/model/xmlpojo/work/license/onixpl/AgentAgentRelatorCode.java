
package org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl;

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
 * <p>Java class for AgentAgentRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="AgentAgentRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:IsA"/>
 *     &lt;enumeration value="onixPL:IsAnyOf"/>
 *     &lt;enumeration value="onixPL:IsAuthorizedRepresentativeOf"/>
 *     &lt;enumeration value="onixPL:IsMemberOf"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "AgentAgentRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum AgentAgentRelatorCode {


    /**
     * The agent belongs to the related agent class.
     */
    @XmlEnumValue("onixPL:IsA")
    ONIX_PL_IS_A("onixPL:IsA"),

    /**
     * The agent is any of the related agents, or belongs to at least one of the related agent classes.
     */
    @XmlEnumValue("onixPL:IsAnyOf")
    ONIX_PL_IS_ANY_OF("onixPL:IsAnyOf"),

    /**
     * The agent is authorized to act for the related agent or agent class.
     */
    @XmlEnumValue("onixPL:IsAuthorizedRepresentativeOf")
    ONIX_PL_IS_AUTHORIZED_REPRESENTATIVE_OF("onixPL:IsAuthorizedRepresentativeOf"),

    /**
     * The agent is a member of the related agent organization.
     */
    @XmlEnumValue("onixPL:IsMemberOf")
    ONIX_PL_IS_MEMBER_OF("onixPL:IsMemberOf");
    private final String value;

    AgentAgentRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AgentAgentRelatorCode fromValue(String v) {
        for (AgentAgentRelatorCode c : AgentAgentRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
