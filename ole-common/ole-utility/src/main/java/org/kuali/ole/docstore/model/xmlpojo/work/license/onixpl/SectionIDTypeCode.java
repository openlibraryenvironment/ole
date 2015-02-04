
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
 * <p>Java class for SectionIDTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="SectionIDTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:DOI"/>
 *     &lt;enumeration value="onixPL:Proprietary"/>
 *     &lt;enumeration value="onixPL:URI"/>
 *     &lt;enumeration value="onixPL:URL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "SectionIDTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum SectionIDTypeCode {


    /**
     * Digital Object Identifier.
     */
    @XmlEnumValue("onixPL:DOI")
    ONIX_PL_DOI("onixPL:DOI"),

    /**
     * An identifier assigned under a proprietary scheme.
     */
    @XmlEnumValue("onixPL:Proprietary")
    ONIX_PL_PROPRIETARY("onixPL:Proprietary"),

    /**
     * Universal Resource Identifier.
     */
    @XmlEnumValue("onixPL:URI")
    ONIX_PL_URI("onixPL:URI"),

    /**
     * Universal Resource Locator.
     */
    @XmlEnumValue("onixPL:URL")
    ONIX_PL_URL("onixPL:URL");
    private final String value;

    SectionIDTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SectionIDTypeCode fromValue(String v) {
        for (SectionIDTypeCode c : SectionIDTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
