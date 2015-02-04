
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
 * <p>Java class for GeneralTermPlaceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="GeneralTermPlaceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AddressForNoticesToLicensee"/>
 *     &lt;enumeration value="onixPL:AddressForNoticesToLicenseeConsortium"/>
 *     &lt;enumeration value="onixPL:AddressForNoticesToLicensor"/>
 *     &lt;enumeration value="onixPL:AddressForNoticesToSublicensee"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "GeneralTermPlaceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum GeneralTermPlaceRelatorCode {


    /**
     * An address (postal, email etc) to which notices may be sent to a licensee.
     */
    @XmlEnumValue("onixPL:AddressForNoticesToLicensee")
    ONIX_PL_ADDRESS_FOR_NOTICES_TO_LICENSEE("onixPL:AddressForNoticesToLicensee"),

    /**
     * An address (postal, email etc) to which notices may be sent to a licensee consortium.
     */
    @XmlEnumValue("onixPL:AddressForNoticesToLicenseeConsortium")
    ONIX_PL_ADDRESS_FOR_NOTICES_TO_LICENSEE_CONSORTIUM("onixPL:AddressForNoticesToLicenseeConsortium"),

    /**
     * An address (postal, email etc) to which notices may be sent to a licensor.
     */
    @XmlEnumValue("onixPL:AddressForNoticesToLicensor")
    ONIX_PL_ADDRESS_FOR_NOTICES_TO_LICENSOR("onixPL:AddressForNoticesToLicensor"),

    /**
     * An address (postal, email etc) to which notices may be sent to a sublicensee.
     */
    @XmlEnumValue("onixPL:AddressForNoticesToSublicensee")
    ONIX_PL_ADDRESS_FOR_NOTICES_TO_SUBLICENSEE("onixPL:AddressForNoticesToSublicensee");
    private final String value;

    GeneralTermPlaceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeneralTermPlaceRelatorCode fromValue(String v) {
        for (GeneralTermPlaceRelatorCode c : GeneralTermPlaceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
