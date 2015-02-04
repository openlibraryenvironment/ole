
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
 * <p>Java class for ResourceIDTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ResourceIDTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:DOI"/>
 *     &lt;enumeration value="onixPL:ISBN13"/>
 *     &lt;enumeration value="onixPL:ISSN"/>
 *     &lt;enumeration value="onixPL:Proprietary"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ResourceIDTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ResourceIDTypeCode {


    /**
     * Digital Object Identifier.
     */
    @XmlEnumValue("onixPL:DOI")
    ONIX_PL_DOI("onixPL:DOI"),

    /**
     * A 13-digit International Standard Book Number.
     */
    @XmlEnumValue("onixPL:ISBN13")
    ONIX_PL_ISBN_13("onixPL:ISBN13"),

    /**
     * An International Standard Serial Number.
     */
    @XmlEnumValue("onixPL:ISSN")
    ONIX_PL_ISSN("onixPL:ISSN"),

    /**
     * An identifier assigned under a proprietary scheme.
     */
    @XmlEnumValue("onixPL:Proprietary")
    ONIX_PL_PROPRIETARY("onixPL:Proprietary");
    private final String value;

    ResourceIDTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResourceIDTypeCode fromValue(String v) {
        for (ResourceIDTypeCode c : ResourceIDTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
