
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
 * <p>Java class for LicenseIDTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseIDTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AgentContractNumber"/>
 *     &lt;enumeration value="onixPL:DOI"/>
 *     &lt;enumeration value="onixPL:LicenseeContractNumber"/>
 *     &lt;enumeration value="onixPL:LicensorContractNumber"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseIDTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseIDTypeCode {


    /**
     * A contract number assigned by an agent.
     */
    @XmlEnumValue("onixPL:AgentContractNumber")
    ONIX_PL_AGENT_CONTRACT_NUMBER("onixPL:AgentContractNumber"),

    /**
     * Digital Object Identifier.
     */
    @XmlEnumValue("onixPL:DOI")
    ONIX_PL_DOI("onixPL:DOI"),

    /**
     * A contract number assigned by a licensee.
     */
    @XmlEnumValue("onixPL:LicenseeContractNumber")
    ONIX_PL_LICENSEE_CONTRACT_NUMBER("onixPL:LicenseeContractNumber"),

    /**
     * A contract number assigned by a licensor.
     */
    @XmlEnumValue("onixPL:LicensorContractNumber")
    ONIX_PL_LICENSOR_CONTRACT_NUMBER("onixPL:LicensorContractNumber");
    private final String value;

    LicenseIDTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseIDTypeCode fromValue(String v) {
        for (LicenseIDTypeCode c : LicenseIDTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
