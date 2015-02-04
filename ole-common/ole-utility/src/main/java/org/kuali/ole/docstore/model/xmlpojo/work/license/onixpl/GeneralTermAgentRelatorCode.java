
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
 * <p>Java class for GeneralTermAgentRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="GeneralTermAgentRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AddresseeForNoticesToLicensee"/>
 *     &lt;enumeration value="onixPL:AddresseeForNoticesToLicensor"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "GeneralTermAgentRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum GeneralTermAgentRelatorCode {


    /**
     * A person to whom notices sent to a licensee should be addressed.
     */
    @XmlEnumValue("onixPL:AddresseeForNoticesToLicensee")
    ONIX_PL_ADDRESSEE_FOR_NOTICES_TO_LICENSEE("onixPL:AddresseeForNoticesToLicensee"),

    /**
     * A person to whom notices sent to a licensor should be addressed.
     */
    @XmlEnumValue("onixPL:AddresseeForNoticesToLicensor")
    ONIX_PL_ADDRESSEE_FOR_NOTICES_TO_LICENSOR("onixPL:AddresseeForNoticesToLicensor");
    private final String value;

    GeneralTermAgentRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeneralTermAgentRelatorCode fromValue(String v) {
        for (GeneralTermAgentRelatorCode c : GeneralTermAgentRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
