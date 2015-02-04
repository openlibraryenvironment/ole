
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
 * <p>Java class for LicenseStatusCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseStatusCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ActiveLicense"/>
 *     &lt;enumeration value="onixPL:Model"/>
 *     &lt;enumeration value="onixPL:NoLongerActive"/>
 *     &lt;enumeration value="onixPL:PolicyTemplate"/>
 *     &lt;enumeration value="onixPL:ProposedLicense"/>
 *     &lt;enumeration value="onixPL:Template"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseStatusCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseStatusCode {


    /**
     * The license is an individual license between specified parties that is currently in force.
     */
    @XmlEnumValue("onixPL:ActiveLicense")
    ONIX_PL_ACTIVE_LICENSE("onixPL:ActiveLicense"),

    /**
     * The license is a model license from which templates or individual licenses may be derived.
     */
    @XmlEnumValue("onixPL:Model")
    ONIX_PL_MODEL("onixPL:Model"),

    /**
     * The license or template has been terminated or replaced.
     */
    @XmlEnumValue("onixPL:NoLongerActive")
    ONIX_PL_NO_LONGER_ACTIVE("onixPL:NoLongerActive"),

    /**
     * The 'license' is a template representing an institution's preferred policies for the use of licensed content.
     */
    @XmlEnumValue("onixPL:PolicyTemplate")
    ONIX_PL_POLICY_TEMPLATE("onixPL:PolicyTemplate"),

    /**
     * The license is a proposed license still under negotiation.
     */
    @XmlEnumValue("onixPL:ProposedLicense")
    ONIX_PL_PROPOSED_LICENSE("onixPL:ProposedLicense"),

    /**
     * The license is a template from which individual licenses are derived.
     */
    @XmlEnumValue("onixPL:Template")
    ONIX_PL_TEMPLATE("onixPL:Template");
    private final String value;

    LicenseStatusCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseStatusCode fromValue(String v) {
        for (LicenseStatusCode c : LicenseStatusCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
