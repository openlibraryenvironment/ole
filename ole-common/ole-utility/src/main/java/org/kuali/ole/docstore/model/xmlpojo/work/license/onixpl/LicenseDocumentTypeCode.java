
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
 * <p>Java class for LicenseDocumentTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseDocumentTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Addendum"/>
 *     &lt;enumeration value="onixPL:License"/>
 *     &lt;enumeration value="onixPL:LicenseMainTerms"/>
 *     &lt;enumeration value="onixPL:LicenseSchedule"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseDocumentTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseDocumentTypeCode {


    /**
     * A document that is added to a license, and that adds to or otherwise amends the terms of the license.
     */
    @XmlEnumValue("onixPL:Addendum")
    ONIX_PL_ADDENDUM("onixPL:Addendum"),

    /**
     * A single document that completely represents an original license, ie one that has no schedules.
     */
    @XmlEnumValue("onixPL:License")
    ONIX_PL_LICENSE("onixPL:License"),

    /**
     * The main terms of an original license, when the complete license comprises main terms and one or more schedules.
     */
    @XmlEnumValue("onixPL:LicenseMainTerms")
    ONIX_PL_LICENSE_MAIN_TERMS("onixPL:LicenseMainTerms"),

    /**
     * An annex, appendix, schedule or other attachment to the main terms of an original license.
     */
    @XmlEnumValue("onixPL:LicenseSchedule")
    ONIX_PL_LICENSE_SCHEDULE("onixPL:LicenseSchedule");
    private final String value;

    LicenseDocumentTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseDocumentTypeCode fromValue(String v) {
        for (LicenseDocumentTypeCode c : LicenseDocumentTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
