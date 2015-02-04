
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
 * <p>Java class for ResourceResourceRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ResourceResourceRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:IsA"/>
 *     &lt;enumeration value="onixPL:IsAnyOf"/>
 *     &lt;enumeration value="onixPL:IsCopyOfPartOf"/>
 *     &lt;enumeration value="onixPL:IsPartOf"/>
 *     &lt;enumeration value="onixPL:IsSumOf"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ResourceResourceRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ResourceResourceRelatorCode {


    /**
     * The resource belongs to the related resource class.
     */
    @XmlEnumValue("onixPL:IsA")
    ONIX_PL_IS_A("onixPL:IsA"),

    /**
     * The resource belongs to at least one of the related resource classes.
     */
    @XmlEnumValue("onixPL:IsAnyOf")
    ONIX_PL_IS_ANY_OF("onixPL:IsAnyOf"),

    /**
     * The resource is a copy of part of the related resource or of part of a resource belonging to the related resource class.
     */
    @XmlEnumValue("onixPL:IsCopyOfPartOf")
    ONIX_PL_IS_COPY_OF_PART_OF("onixPL:IsCopyOfPartOf"),

    /**
     * The resource is part of the related resource or of a resource belonging to the related resource class.
     */
    @XmlEnumValue("onixPL:IsPartOf")
    ONIX_PL_IS_PART_OF("onixPL:IsPartOf"),

    /**
     * The resource is the totality of all the related resources.
     */
    @XmlEnumValue("onixPL:IsSumOf")
    ONIX_PL_IS_SUM_OF("onixPL:IsSumOf");
    private final String value;

    ResourceResourceRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResourceResourceRelatorCode fromValue(String v) {
        for (ResourceResourceRelatorCode c : ResourceResourceRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
