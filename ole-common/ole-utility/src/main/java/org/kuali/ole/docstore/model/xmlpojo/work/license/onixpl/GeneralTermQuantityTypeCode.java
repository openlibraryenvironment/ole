
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
 * <p>Java class for GeneralTermQuantityTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="GeneralTermQuantityTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:NoticePeriodForNonRenewal"/>
 *     &lt;enumeration value="onixPL:NoticePeriodForTermination"/>
 *     &lt;enumeration value="onixPL:NoticePeriodForTerminationByBreach"/>
 *     &lt;enumeration value="onixPL:PeriodForCureOfBreach"/>
 *     &lt;enumeration value="onixPL:RenewalPeriod"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "GeneralTermQuantityTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum GeneralTermQuantityTypeCode {


    /**
     * The number of days, weeks or months notice that is required for non-renewal of a self-renewing license at the end of a current term.
     */
    @XmlEnumValue("onixPL:NoticePeriodForNonRenewal")
    ONIX_PL_NOTICE_PERIOD_FOR_NON_RENEWAL("onixPL:NoticePeriodForNonRenewal"),

    /**
     * The number of days, weeks or months notice that is required for termination of a license for reasons other than breach.
     */
    @XmlEnumValue("onixPL:NoticePeriodForTermination")
    ONIX_PL_NOTICE_PERIOD_FOR_TERMINATION("onixPL:NoticePeriodForTermination"),

    /**
     * The number of days, weeks or months notice that is required for termination of a license in the event of breach by one of the parties.
     */
    @XmlEnumValue("onixPL:NoticePeriodForTerminationByBreach")
    ONIX_PL_NOTICE_PERIOD_FOR_TERMINATION_BY_BREACH("onixPL:NoticePeriodForTerminationByBreach"),

    /**
     * The number of days, weeks or months that are allowed for remedying a breach after the breach has been notified.
     */
    @XmlEnumValue("onixPL:PeriodForCureOfBreach")
    ONIX_PL_PERIOD_FOR_CURE_OF_BREACH("onixPL:PeriodForCureOfBreach"),

    /**
     * The number of days, weeks, months or years in a renewal period specified in a license.
     */
    @XmlEnumValue("onixPL:RenewalPeriod")
    ONIX_PL_RENEWAL_PERIOD("onixPL:RenewalPeriod");
    private final String value;

    GeneralTermQuantityTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeneralTermQuantityTypeCode fromValue(String v) {
        for (GeneralTermQuantityTypeCode c : GeneralTermQuantityTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
