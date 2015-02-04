
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
 * <p>Java class for RelatedTimePointCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="RelatedTimePointCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:LicenseStartDate"/>
 *     &lt;enumeration value="onixPL:LicenseEndDate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "RelatedTimePointCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum RelatedTimePointCode {


    /**
     * The date from which a license is effective.
     */
    @XmlEnumValue("onixPL:LicenseStartDate")
    ONIX_PL_LICENSE_START_DATE("onixPL:LicenseStartDate"),

    /**
     * The date on which a license terminates unless renewed.
     */
    @XmlEnumValue("onixPL:LicenseEndDate")
    ONIX_PL_LICENSE_END_DATE("onixPL:LicenseEndDate");
    private final String value;

    RelatedTimePointCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelatedTimePointCode fromValue(String v) {
        for (RelatedTimePointCode c : RelatedTimePointCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
