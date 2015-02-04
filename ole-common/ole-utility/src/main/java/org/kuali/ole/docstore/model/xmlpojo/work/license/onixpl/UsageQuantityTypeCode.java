
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
 * <p>Java class for UsageQuantityTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="UsageQuantityTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:NumberOfConcurrentUsers"/>
 *     &lt;enumeration value="onixPL:NumberOfPooledConcurrentUsers"/>
 *     &lt;enumeration value="onixPL:NumberOfCopiesPermitted"/>
 *     &lt;enumeration value="onixPL:RetentionPeriodForUsedResource"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "UsageQuantityTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum UsageQuantityTypeCode {


    /**
     * The number of users simultaneously performing a usage.
     */
    @XmlEnumValue("onixPL:NumberOfConcurrentUsers")
    ONIX_PL_NUMBER_OF_CONCURRENT_USERS("onixPL:NumberOfConcurrentUsers"),

    /**
     * The number of users simultaneously performing a usage, if shared across a consortium rather than within a single institution.
     */
    @XmlEnumValue("onixPL:NumberOfPooledConcurrentUsers")
    ONIX_PL_NUMBER_OF_POOLED_CONCURRENT_USERS("onixPL:NumberOfPooledConcurrentUsers"),

    /**
     * The number of copies made under a usage.
     */
    @XmlEnumValue("onixPL:NumberOfCopiesPermitted")
    ONIX_PL_NUMBER_OF_COPIES_PERMITTED("onixPL:NumberOfCopiesPermitted"),

    /**
     * The time period during which the used resource(s) may be retained.
     */
    @XmlEnumValue("onixPL:RetentionPeriodForUsedResource")
    ONIX_PL_RETENTION_PERIOD_FOR_USED_RESOURCE("onixPL:RetentionPeriodForUsedResource");
    private final String value;

    UsageQuantityTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UsageQuantityTypeCode fromValue(String v) {
        for (UsageQuantityTypeCode c : UsageQuantityTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
