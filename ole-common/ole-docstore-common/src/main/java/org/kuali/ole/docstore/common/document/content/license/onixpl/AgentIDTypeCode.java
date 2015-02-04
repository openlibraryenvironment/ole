
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
 * <p>Java class for AgentIDTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="AgentIDTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:CompanyRegistrationNumber"/>
 *     &lt;enumeration value="onixPL:Proprietary"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "AgentIDTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum AgentIDTypeCode {


    /**
     * A number under which a company is officially registered.
     */
    @XmlEnumValue("onixPL:CompanyRegistrationNumber")
    ONIX_PL_COMPANY_REGISTRATION_NUMBER("onixPL:CompanyRegistrationNumber"),

    /**
     * An identifier assigned under a proprietary scheme.
     */
    @XmlEnumValue("onixPL:Proprietary")
    ONIX_PL_PROPRIETARY("onixPL:Proprietary");
    private final String value;

    AgentIDTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AgentIDTypeCode fromValue(String v) {
        for (AgentIDTypeCode c : AgentIDTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
