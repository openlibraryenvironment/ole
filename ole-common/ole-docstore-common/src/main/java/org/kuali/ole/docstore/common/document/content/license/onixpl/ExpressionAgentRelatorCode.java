
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
 * <p>Java class for ExpressionAgentRelatorCode.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p/>
 * <pre>
 * &lt;simpleType name="ExpressionAgentRelatorCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="onixPL:ApprovedBy"/>
 *     &lt;enumeration value="onixPL:PreparedBy"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "ExpressionAgentRelatorCode", namespace = "http://www.editeur.org/onix-pl")
@XmlEnum
public enum ExpressionAgentRelatorCode {


    /**
     * The person who approved a license expression.
     */
    @XmlEnumValue("onixPL:ApprovedBy")
    ONIX_PL_APPROVED_BY("onixPL:ApprovedBy"),

    /**
     * The person who prepared a license expression.
     */
    @XmlEnumValue("onixPL:PreparedBy")
    ONIX_PL_PREPARED_BY("onixPL:PreparedBy");
    private final String value;

    ExpressionAgentRelatorCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExpressionAgentRelatorCode fromValue(String v) {
        for (ExpressionAgentRelatorCode c : ExpressionAgentRelatorCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
