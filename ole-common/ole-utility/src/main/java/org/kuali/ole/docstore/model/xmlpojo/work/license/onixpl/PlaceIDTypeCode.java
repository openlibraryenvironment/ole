
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
 * <p>Java class for PlaceIDTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="PlaceIDTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:EmailAddress"/>
 *     &lt;enumeration value="onixPL:ISOCountryCode"/>
 *     &lt;enumeration value="onixPL:LocationCode"/>
 *     &lt;enumeration value="onixPL:Proprietary"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "PlaceIDTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum PlaceIDTypeCode {


    /**
     * An email address for a person or organization.
     */
    @XmlEnumValue("onixPL:EmailAddress")
    ONIX_PL_EMAIL_ADDRESS("onixPL:EmailAddress"),

    /**
     * An ISO 3166/1 two-letter country code.
     */
    @XmlEnumValue("onixPL:ISOCountryCode")
    ONIX_PL_ISO_COUNTRY_CODE("onixPL:ISOCountryCode"),

    /**
     * An ONIX-maintained code for a location that is not a country, mainly used in connection with law and jurisdiction, eg for a state or province or part of the UK. Based where possible on the UN/LOCODE standard.
     */
    @XmlEnumValue("onixPL:LocationCode")
    ONIX_PL_LOCATION_CODE("onixPL:LocationCode"),

    /**
     * An identifier assigned under a proprietary scheme.
     */
    @XmlEnumValue("onixPL:Proprietary")
    ONIX_PL_PROPRIETARY("onixPL:Proprietary");
    private final String value;

    PlaceIDTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PlaceIDTypeCode fromValue(String v) {
        for (PlaceIDTypeCode c : PlaceIDTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
