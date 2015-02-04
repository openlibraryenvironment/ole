
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
 * <p>Java class for LicenseGrantTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseGrantTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Exclusive"/>
 *     &lt;enumeration value="onixPL:NonExclusive"/>
 *     &lt;enumeration value="onixPL:NonTransferable"/>
 *     &lt;enumeration value="onixPL:Transferable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseGrantTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseGrantTypeCode {


    /**
     * License is exclusive to the licensee.
     */
    @XmlEnumValue("onixPL:Exclusive")
    ONIX_PL_EXCLUSIVE("onixPL:Exclusive"),

    /**
     * License is not exclusive to the licensee.
     */
    @XmlEnumValue("onixPL:NonExclusive")
    ONIX_PL_NON_EXCLUSIVE("onixPL:NonExclusive"),

    /**
     * License is not transferable from the licensee to another party.
     */
    @XmlEnumValue("onixPL:NonTransferable")
    ONIX_PL_NON_TRANSFERABLE("onixPL:NonTransferable"),

    /**
     * License may be transferred from the licensee to another party.
     */
    @XmlEnumValue("onixPL:Transferable")
    ONIX_PL_TRANSFERABLE("onixPL:Transferable");
    private final String value;

    LicenseGrantTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseGrantTypeCode fromValue(String v) {
        for (LicenseGrantTypeCode c : LicenseGrantTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
