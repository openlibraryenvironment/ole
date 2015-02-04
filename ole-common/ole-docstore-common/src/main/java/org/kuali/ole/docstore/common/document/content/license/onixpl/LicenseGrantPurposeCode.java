
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
 * <p>Java class for LicenseGrantPurposeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseGrantPurposeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:AcademicUse"/>
 *     &lt;enumeration value="onixPL:NonCommercialUse"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseGrantPurposeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseGrantPurposeCode {


    /**
     * Use for the purpose of education, teaching, private study and/or academic research.
     */
    @XmlEnumValue("onixPL:AcademicUse")
    ONIX_PL_ACADEMIC_USE("onixPL:AcademicUse"),

    /**
     * Use that is not for the purpose of monetary reward.
     */
    @XmlEnumValue("onixPL:NonCommercialUse")
    ONIX_PL_NON_COMMERCIAL_USE("onixPL:NonCommercialUse");
    private final String value;

    LicenseGrantPurposeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseGrantPurposeCode fromValue(String v) {
        for (LicenseGrantPurposeCode c : LicenseGrantPurposeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
