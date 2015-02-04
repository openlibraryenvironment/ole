
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
 * <p>Java class for UsageResourceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageResourceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:TargetResource"/>
 *     &lt;enumeration value="onixPL:MustInclude"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageResourceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageResourceRelatorCode {


    /**
     * A resource that is created, modified or added to by a usage.
     */
    @XmlEnumValue("onixPL:TargetResource")
    ONIX_PL_TARGET_RESOURCE("onixPL:TargetResource"),

    /**
     * Content that must be included with a resource when it is used in a usage.
     */
    @XmlEnumValue("onixPL:MustInclude")
    ONIX_PL_MUST_INCLUDE("onixPL:MustInclude");
    private final String value;

    UsageResourceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageResourceRelatorCode fromValue(String v) {
        for (UsageResourceRelatorCode c : UsageResourceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
