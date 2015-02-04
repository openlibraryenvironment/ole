
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
 * <p>Java class for ExpressionStatusCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ExpressionStatusCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:Approved"/>
 *     &lt;enumeration value="onixPL:Complete"/>
 *     &lt;enumeration value="onixPL:Draft"/>
 *     &lt;enumeration value="onixPL:Replaced"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ExpressionStatusCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ExpressionStatusCode {


    /**
     * The expression is approved for use.
     */
    @XmlEnumValue("onixPL:Approved")
    ONIX_PL_APPROVED("onixPL:Approved"),

    /**
     * The expression is considered complete, but has not been through a formal approval process.
     */
    @XmlEnumValue("onixPL:Complete")
    ONIX_PL_COMPLETE("onixPL:Complete"),

    /**
     * The expression is a draft, awaiting completion and/or approval.
     */
    @XmlEnumValue("onixPL:Draft")
    ONIX_PL_DRAFT("onixPL:Draft"),

    /**
     * The expression has been superseded by a later version.
     */
    @XmlEnumValue("onixPL:Replaced")
    ONIX_PL_REPLACED("onixPL:Replaced");
    private final String value;

    ExpressionStatusCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExpressionStatusCode fromValue(String v) {
        for (ExpressionStatusCode c : ExpressionStatusCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
