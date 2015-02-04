
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
 * <p>Java class for PaymentTermTypeCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="PaymentTermTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:PaymentConditions"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "PaymentTermTypeCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum PaymentTermTypeCode {


    /**
     * A license term that specifies conditions relating to a licensee's obligation to make payments under a license.
     */
    @XmlEnumValue("onixPL:PaymentConditions")
    ONIX_PL_PAYMENT_CONDITIONS("onixPL:PaymentConditions");
    private final String value;

    PaymentTermTypeCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PaymentTermTypeCode fromValue(String v) {
        for (PaymentTermTypeCode c : PaymentTermTypeCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
