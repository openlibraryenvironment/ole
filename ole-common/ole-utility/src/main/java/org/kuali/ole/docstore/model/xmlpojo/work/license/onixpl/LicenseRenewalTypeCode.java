
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
 * <p>Java class for LicenseRenewalTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseRenewalTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Automatic"/>
 *     &lt;enumeration value="onixPL:Explicit"/>
 *     &lt;enumeration value="onixPL:NonRenewable"/>
 *     &lt;enumeration value="onixPL:Perpetual"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseRenewalTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseRenewalTypeCode {


    /**
     * License renews automatically for successive periods unless explicitly cancelled.
     */
    @XmlEnumValue("onixPL:Automatic")
    ONIX_PL_AUTOMATIC("onixPL:Automatic"),

    /**
     * License terminates at the end of the current term unless explicitly renewed.
     */
    @XmlEnumValue("onixPL:Explicit")
    ONIX_PL_EXPLICIT("onixPL:Explicit"),

    /**
     * License cannot be renewed: a new license must be issued at the end of the current term.
     */
    @XmlEnumValue("onixPL:NonRenewable")
    ONIX_PL_NON_RENEWABLE("onixPL:NonRenewable"),

    /**
     * License continues in perpetuity.
     */
    @XmlEnumValue("onixPL:Perpetual")
    ONIX_PL_PERPETUAL("onixPL:Perpetual");
    private final String value;

    LicenseRenewalTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseRenewalTypeCode fromValue(String v) {
        for (LicenseRenewalTypeCode c : LicenseRenewalTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
