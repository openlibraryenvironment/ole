
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
 * <p>Java class for LicensePlaceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicensePlaceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AddressForNoticesToLicensee"/>
 *     &lt;enumeration value="onixPL:AddressForNoticesToLicenseeConsortium"/>
 *     &lt;enumeration value="onixPL:AddressForNoticesToLicensor"/>
 *     &lt;enumeration value="onixPL:AddressForNoticesToSublicensor"/>
 *     &lt;enumeration value="onixPL:LicenseeSite"/>
 *     &lt;enumeration value="onixPL:PlaceOfCopyrightLaw"/>
 *     &lt;enumeration value="onixPL:PlaceOfGoverningLaw"/>
 *     &lt;enumeration value="onixPL:PlaceOfJurisdiction"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicensePlaceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicensePlaceRelatorCode {


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
    @XmlEnumValue("onixPL:AddressForNoticesToSublicensor")
    ONIX_PL_ADDRESS_FOR_NOTICES_TO_SUBLICENSOR("onixPL:AddressForNoticesToSublicensor"),

    /**
     * A site occupied by the licensee at which the license is applicable.
     */
    @XmlEnumValue("onixPL:LicenseeSite")
    ONIX_PL_LICENSEE_SITE("onixPL:LicenseeSite"),

    /**
     * A country or other place whose copyright law is specified as applicable to a license.
     */
    @XmlEnumValue("onixPL:PlaceOfCopyrightLaw")
    ONIX_PL_PLACE_OF_COPYRIGHT_LAW("onixPL:PlaceOfCopyrightLaw"),

    /**
     * A country or other place under whose laws a license is to be interpreted.
     */
    @XmlEnumValue("onixPL:PlaceOfGoverningLaw")
    ONIX_PL_PLACE_OF_GOVERNING_LAW("onixPL:PlaceOfGoverningLaw"),

    /**
     * A country or other place whose jurisdiction is specified in a license.
     */
    @XmlEnumValue("onixPL:PlaceOfJurisdiction")
    ONIX_PL_PLACE_OF_JURISDICTION("onixPL:PlaceOfJurisdiction");
    private final String value;

    LicensePlaceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicensePlaceRelatorCode fromValue(String v) {
        for (LicensePlaceRelatorCode c : LicensePlaceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
