
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
 * <p>Java class for LicenseTimePointRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="LicenseTimePointRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:InitialTermEndDate"/>
 *     &lt;enumeration value="onixPL:LicenseEndDate"/>
 *     &lt;enumeration value="onixPL:LicenseExecutionDate"/>
 *     &lt;enumeration value="onixPL:LicenseStartDate"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "LicenseTimePointRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum LicenseTimePointRelatorCode {


    /**
     * The date when the initial term of a license ends or ended.
     */
    @XmlEnumValue("onixPL:InitialTermEndDate")
    ONIX_PL_INITIAL_TERM_END_DATE("onixPL:InitialTermEndDate"),

    /**
     * The date when the term of a license ends or ended.
     */
    @XmlEnumValue("onixPL:LicenseEndDate")
    ONIX_PL_LICENSE_END_DATE("onixPL:LicenseEndDate"),

    /**
     * The date when a license was executed.
     */
    @XmlEnumValue("onixPL:LicenseExecutionDate")
    ONIX_PL_LICENSE_EXECUTION_DATE("onixPL:LicenseExecutionDate"),

    /**
     * The date when a license becomes or became effective.
     */
    @XmlEnumValue("onixPL:LicenseStartDate")
    ONIX_PL_LICENSE_START_DATE("onixPL:LicenseStartDate");
    private final String value;

    LicenseTimePointRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LicenseTimePointRelatorCode fromValue(String v) {
        for (LicenseTimePointRelatorCode c : LicenseTimePointRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
