
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
 * <p>Java class for LicenseResourceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseResourceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:LicensedContent"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseResourceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseResourceRelatorCode {


    /**
     * Material that is licensed under a license.
     */
    @XmlEnumValue("onixPL:LicensedContent")
    ONIX_PL_LICENSED_CONTENT("onixPL:LicensedContent");
    private final String value;

    LicenseResourceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseResourceRelatorCode fromValue(String v) {
        for (LicenseResourceRelatorCode c : LicenseResourceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
