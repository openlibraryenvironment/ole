
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
 * <p>Java class for AgentPlaceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="AgentPlaceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:PlaceOfCorporateRegistration"/>
 *     &lt;enumeration value="onixPL:PrincipalPlaceOfBusiness"/>
 *     &lt;enumeration value="onixPL:RegisteredAddress"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "AgentPlaceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum AgentPlaceRelatorCode {


    /**
     * The country or state where an organization is legally registered.
     */
    @XmlEnumValue("onixPL:PlaceOfCorporateRegistration")
    ONIX_PL_PLACE_OF_CORPORATE_REGISTRATION("onixPL:PlaceOfCorporateRegistration"),

    /**
     * The address at which a person or organization has a principal place of business.
     */
    @XmlEnumValue("onixPL:PrincipalPlaceOfBusiness")
    ONIX_PL_PRINCIPAL_PLACE_OF_BUSINESS("onixPL:PrincipalPlaceOfBusiness"),

    /**
     * The officially registered address of an organization.
     */
    @XmlEnumValue("onixPL:RegisteredAddress")
    ONIX_PL_REGISTERED_ADDRESS("onixPL:RegisteredAddress");
    private final String value;

    AgentPlaceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AgentPlaceRelatorCode fromValue(String v) {
        for (AgentPlaceRelatorCode c : AgentPlaceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
