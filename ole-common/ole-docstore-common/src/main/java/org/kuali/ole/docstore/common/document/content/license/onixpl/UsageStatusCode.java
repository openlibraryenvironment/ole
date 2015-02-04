
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
 * <p>Java class for UsageStatusCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageStatusCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:InterpretedAsPermitted"/>
 *     &lt;enumeration value="onixPL:InterpretedAsProhibited"/>
 *     &lt;enumeration value="onixPL:Permitted"/>
 *     &lt;enumeration value="onixPL:Prohibited"/>
 *     &lt;enumeration value="onixPL:SilentUninterpreted"/>
 *     &lt;enumeration value="onixPL:NotApplicable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageStatusCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageStatusCode {


    /**
     * Interpreted as permitted under a license.
     */
    @XmlEnumValue("onixPL:InterpretedAsPermitted")
    ONIX_PL_INTERPRETED_AS_PERMITTED("onixPL:InterpretedAsPermitted"),

    /**
     * Interpreted as prohibited under a license.
     */
    @XmlEnumValue("onixPL:InterpretedAsProhibited")
    ONIX_PL_INTERPRETED_AS_PROHIBITED("onixPL:InterpretedAsProhibited"),

    /**
     * Explicitly permitted under a license.
     */
    @XmlEnumValue("onixPL:Permitted")
    ONIX_PL_PERMITTED("onixPL:Permitted"),

    /**
     * Explicitly prohibited under a license.
     */
    @XmlEnumValue("onixPL:Prohibited")
    ONIX_PL_PROHIBITED("onixPL:Prohibited"),

    /**
     * License is silent, and no interpretation has been made.
     */
    @XmlEnumValue("onixPL:SilentUninterpreted")
    ONIX_PL_SILENT_UNINTERPRETED("onixPL:SilentUninterpreted"),

    /**
     * Not applicable in the particular context of a license.
     */
    @XmlEnumValue("onixPL:NotApplicable")
    ONIX_PL_NOT_APPLICABLE("onixPL:NotApplicable");
    private final String value;

    UsageStatusCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageStatusCode fromValue(String v) {
        for (UsageStatusCode c : UsageStatusCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
